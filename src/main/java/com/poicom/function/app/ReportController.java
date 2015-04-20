package com.poicom.function.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;
import com.poicom.common.controller.JFController;
import com.poicom.common.kit.AlertKit;
import com.poicom.common.kit.DateKit;
import com.poicom.function.app.model.ErrorType;
import com.poicom.function.app.model.Level;
import com.poicom.function.app.model.Order;
import com.poicom.function.system.model.User;
import com.poicom.function.system.model.UserInfo;

/**
 * @描述 故障申报
 * @author poicom7
 *
 */
@ControllerKey(value = "/report", path = "/page/app/report")
public class ReportController extends JFController{
	
	protected Logger logger=LoggerFactory.getLogger(ReportController.class);
	
	private final static String REPORT_QUERY_PAGE="query.html";
	
	public void index(){
		
		render("order");
	}

	
	
	/**
	 * @描述 报障人员查询本账号申报的故障工单
	 */
	public void reports(){
		User user=User.dao.getCurrentUser();
		Page<Record> reportPage;
		String orderby=" ORDER BY o.offer_at DESC ";
		
		if(ValidateKit.isNullOrEmpty(getPara("selectType"))){
			String where=" WHERE o.offer_user=? ";
			reportPage=Order.dao.findReportsByUserId(getParaToInt(0,1), 10,where,orderby,user.get("id"));
		}else{
			String where=" WHERE o.offer_user=? and o.type=? ";
			reportPage=Order.dao.findReportsByUserId(getParaToInt(0,1), 10,where,orderby,user.get("id"),getParaToInt("selectType"));
		}
		
		
		Order.dao.format(reportPage,"description");
		
		setAttr("reportPage",reportPage);
		setAttr("typeList",ErrorType.dao.getAllType());
		setAttr("typeid",getPara("selectType"));
		render("report.html");
	}
	
	/**
	 * 查询故障工单详细内容
	 */
	public void report(){
		String where="o.id=?";
		Record order = Order.dao.findReportById(where,getParaToInt("id"));

		//获取工单申报者的分公司信息
		Record offer=UserInfo.dao.getUserBranch(order.getLong("oofferid"));
		//获取工单处理者的分公司信息
		Record deal=UserInfo.dao.getUserBranch(order.getLong("odealid"));
		
		if(!ValidateKit.isNullOrEmpty(offer)){
			setAttr("offer_branch",offer.getStr("bname"));
			setAttr("offer",offer);
		}
		
		if(!ValidateKit.isNullOrEmpty(deal)){
			setAttr("deal_branch",deal.getStr("bname"));
			setAttr("deal",deal);
		}
			
		//工单详细信息
		setAttr(order);
		setAttr("order", order);
		//故障类型
		setAttr("typeList",ErrorType.dao.findAll());
		setAttr("levelList",Level.dao.findAll());

		render(REPORT_QUERY_PAGE);
	}
	
	/**
	 * 超时工单催办处理
	 */
	public void hasten(){
		int id=getParaToInt("id");
		
		Order order=Order.dao.findById(id);
		order.set("flag", 1).update();
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		//运维人员列表
		List<Record> dealList=User.dao.getOperatorList(order.get("type"));
		List<String> phones=new ArrayList<String>();
		//发送邮件
		for (Record deal : dealList) {
			//获取邮件内容
			String body=AlertKit.getMailBody(userinfo,deal,order).toString();
			//发送邮件通知
			if(ValidateKit.isEmail(deal.getStr("useremail"))){
				AlertKit.sendEmail("故障申报超时提醒！",body,deal.getStr("useremail"));
			}
			//电话&&非空 then 保存电话列表
			if(!ValidateKit.isNullOrEmpty(deal.getStr("userphone"))){
				if(ValidateKit.isPhone(deal.getStr("userphone"))){
					phones.add(deal.getStr("userphone"));
				}
			}
			
		}
		//发送短信
		String[] array =new String[phones.size()];
		//AlertKit.sendSms(userinfo,order,phones.toArray(array));
		
		redirect("/report/reports");
		
	}
	
	/**
	 * @描述 对提交故障工单进行撤回
	 */
	public void recall(){
		int id=getParaToInt("id");
		Order order=Order.dao.findById(id);
		order.set("deleted_at", DateKit.format(new Date(),DateKit.pattern_ymd_hms)).update();
		
		redirect("/report/reports");
		
	}

	
	/**
	 * @描述 进入新建故障工单页面
	 */
	public void offer(){
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		
		setAttr(userinfo);
		
		setAttr("userinfo",userinfo);
		setAttr("typeList",ErrorType.dao.getAllType());
		setAttr("levelList",Level.dao.findAll());
		render("add.html");
	}
	
	
	/**
	 * @描述 新建故障工单 并发送邮件、短信通知
	 */
	//@Before( {Tx.class,CommonValidator.class,EvictInterceptor.class})
	@Before( {Tx.class,CommonValidator.class})
	//@CacheName("/order/query")
	public void save(){
		
		//获取表单数据，填充进Order
		Order order=new Order().set("offer_user", getParaToInt("uuserid"))
				.set("description", getPara("odescription"))
				.set("type", getParaToInt("selectType"))
				.set("level", getPara("selectLevel"))
				.set("status", 1)
				.set("offer_at", 
						DateTime.now().toString("yyyy-MM-dd HH:mm:ss"))
				.set("flag", 0);
		if(order.save())
			redirect("/report/offer");
		else
			System.out.println("userid:" + getParaToInt("uuserid") + " type:"
					+ getParaToInt("selectType") + " des:"
					+ getPara("odescription"));
		
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		//运维人员列表
		List<Record> dealList=User.dao.getOperatorList(getParaToInt("selectType"));
		List<String> phones=new ArrayList<String>();
		//发送邮件
		for (Record deal : dealList) {
			//获取邮件内容
			String body=AlertKit.getMailBody(userinfo,getPara("odescription"),deal.getStr("fullname")).toString();
			//发送邮件通知
			if(ValidateKit.isEmail(deal.getStr("useremail"))){
				AlertKit.sendEmail("点通故障系统提醒您！",body,deal.getStr("useremail"));
			}
			//电话&&非空 then 保存电话列表
			if(!ValidateKit.isNullOrEmpty(deal.getStr("userphone"))){
				if(ValidateKit.isPhone(deal.getStr("userphone"))){
					phones.add(deal.getStr("userphone"));
				}
			}
			
		}
		//发送短信
		String[] array =new String[phones.size()];
		//AlertKit.sendSms(userinfo,phones.toArray(array));
		
		
		redirect("/report/reports");
	}
	
	/**
	 * @描述 申报人员可以修改未被处理的工单
	 *            此时不会再次发送邮件、短信通知。
	 */
	public void edit(){
		
		//故障类型
		setAttr("typeList",ErrorType.dao.getAllType());
		//
		setAttr("levelList",Level.dao.findAll());
		
		String where="o.id=?";
		//工单详细信息
		Record order = Order.dao.findReportById(where,getParaToInt("id"));
		setAttr(order);
		setAttr("order", order);
		
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		setAttr(userinfo);
		setAttr("userinfo",userinfo);
		
		//获取工单申报者的分公司信息
		Record offer=UserInfo.dao.getUserBranch(order.getLong("offerid"));
		if(!ValidateKit.isNullOrEmpty(offer))
			setAttr("offer_branch",offer.getStr("branch"));
	}
	
	/**
	 *  @描述 处理申报人员更新故障工单操作 
	 */
	//@Before( {Tx.class,CommonValidator.class,EvictInterceptor.class})
	@Before( CommonValidator.class)
	//@CacheName("/order/query")
	public void update(){
		//order_id
		Integer orderid=getParaToInt("oorderid");
		//description
		//String description=getPara("odescription");
		
		Order.dao
				.findById(orderid)
				.set("description", getPara("odescription"))
				.set("updated_at",
						DateTime.now().toString("yyyy-MM-dd HH:mm:ss"))
				.update();

		redirect("/report/reports");
		
	}
	
	/**
	 * @描述 电话数组格式化
	 */
	/*private String phoneFormat(String... phone){
		StringBuffer p=new StringBuffer();
		for(int i=0;i<phone.length;i++){
			if(i==(phone.length-1)){
				p.append(phone[i]);
			}else
				p.append(phone[i]).append(",");
		}
		//电话列表...
		return p.toString();
		
	}*/
	
	/**
	 * @描述 发送短信通知
	 * @param userinfo
	 * @param phone
	 */
	/*public void sendSms(Record userinfo,String... phone ){
		//电话列表...
		String phones=phoneFormat(phone);
		System.out.println(phones);
		
		String department="XX";
		String name="XX";
		String type="XX";
		
		
		StringBuffer contt=new StringBuffer();
		contt.append("您好，")
				.append(userinfo.getStr("bname")+"的 ")
				.append(userinfo.getStr("ufullname"))
				.append(" ("+userinfo.getStr("uphone")+") ")
				.append("提交了故障工单，请尽快处理。");
		String content =contt.toString();

		try {
			
			URL url=new URL("http://sms.poicom.net/postsms.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");// 提交模式
			
			conn.setDoOutput(true);// 是否输入参数
			
			StringBuffer params = new StringBuffer();
	        // 表单参数与get形式一样
	        params.append("phones").append("=").append(phones).append("&")
	              .append("department").append("=").append(department).append("&")
	              .append("name").append("=").append(name).append("&")
	              .append("type").append("=").append(type).append("&")
	              .append("content").append("=").append(content);
	        byte[] bypes = params.toString().getBytes();
	        OutputStream outStream= conn.getOutputStream();
	        outStream.write(bypes);// 输入参数
	        outStream.flush();
	        outStream.close();
	        
	        System.out.println(conn.getResponseCode()); //响应代码 200表示成功

			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
	
	

}
