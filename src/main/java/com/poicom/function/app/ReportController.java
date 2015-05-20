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
import com.poicom.common.kit.WebKit;
import com.poicom.common.thread.ThreadAlert;
import com.poicom.function.app.model.Apartment;
import com.poicom.function.app.model.Comment;
import com.poicom.function.app.model.ErrorType;
import com.poicom.function.app.model.Level;
import com.poicom.function.app.model.Order;
import com.poicom.function.app.model.UserOrder;
import com.poicom.function.system.model.User;
import com.poicom.function.system.model.UserInfo;

/**
 * @描述 故障申报
 * @author 唐东宇
 *
 */
@ControllerKey(value = "/report", path = "/app/report")
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
		
		//拼接where语句
		StringBuffer whereadd=new StringBuffer();
		//保存condition条件
		List<Object> conditions=new ArrayList<Object>();
		String title=getPara("title");
		String branch=getPara("branch");
		String offeruser=getPara("offeruser");
		String offertime=getPara("offertime");
		
		//若无查询条件 则按正常查询。
		if(ValidateKit.isNullOrEmpty(title)
				&&ValidateKit.isNullOrEmpty(branch)
				&&ValidateKit.isNullOrEmpty(offeruser)
				&&ValidateKit.isNullOrEmpty(offertime)){
			
			String where=" WHERE o.offer_user=? ";
			String orderby=" ORDER BY o.deleted_at ASC,o.status DESC, o.offer_at DESC ";
			reportPage=Order.dao.findReportsByUserId(getParaToInt(0,1), 10,where,orderby, user.get("id"));
			Order.dao.format(reportPage,"title");
			setAttr("reportPage",reportPage);
			
		}else{
			
			conditions.add(user.get("id"));
			//查询申报人
			if(!ValidateKit.isNullOrEmpty(offeruser)){
				whereadd.append(" and u1.full_name like ? ");
				conditions.add("%"+getPara("offeruser").trim()+"%");
				setAttr("offeruser",offeruser);
			}
			//查询申报人公司
			if(!ValidateKit.isNullOrEmpty(branch)){
				whereadd.append(" and u1.bname like ? ");
				conditions.add("%"+getPara("branch").trim()+"%");
				setAttr("branch",branch);
			}
			//查询标题
			if(!ValidateKit.isNullOrEmpty(title)){
				whereadd.append(" and o.title like ? ");
				conditions.add("%"+getPara("title").trim()+"%");
				setAttr("title",title);
			}
			//查询申报时间
			if(!ValidateKit.isNullOrEmpty(offertime)){
				whereadd.append(" and o.offer_at like ? ");
				conditions.add(getPara("offertime").trim()+"%");
			}
			String where=" WHERE o.offer_user=? "+whereadd.toString();
			String orderby=" ORDER BY o.deleted_at ASC,o.status DESC, o.offer_at DESC ";
			Object[] condition= new Object[conditions.size()];
			conditions.toArray(condition);
			reportPage=Order.dao.findReportsByUserId(getParaToInt(0,1), 10,where,orderby,condition);
			Order.dao.format(reportPage,"title");
			setAttr("reportPage",reportPage);
			
		}
		
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
		
		if(!ValidateKit.isNullOrEmpty(offer)){
			setAttr("offer",offer);
		}
		//获取工单处理意见
		List<Record> commentList=Comment.dao.findCommentsByOrderId(" comments.order_id=? order by comments.add_at asc ",order.getLong("oorderid"));
		setAttr("commentList",commentList);
			
		//工单详细信息
		setAttr(order);

		render(REPORT_QUERY_PAGE);
	}
	
	/**
	 * 超时工单催办处理
	 */
	@Deprecated
	public void hasten(){
		int id=getParaToInt("oorderid");
		
		if(ValidateKit.isNullOrEmpty(getParaToInt("oorderid"))){
			renderJson("state","操作失败！");
		}else{
			
			Order order=Order.dao.findById(id);
			if(order.get("flag").equals(1)){
				renderJson("state","操作失败，已催办！");
			}else{
				order.set("flag", 1).update();
				
				//当前用户详细信息
				Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
				//运维人员列表
				List<Record> dealList=new ArrayList<Record>();
				//运维人员详细信息-接收
				List<UserOrder> userOrderList=UserOrder.dao.findBy(" order_id=?", id);
				for(UserOrder userorder:userOrderList){
					Record accept_user=UserInfo.dao.getAllUserInfo(userorder.get("user_id"));
					dealList.add(accept_user);
				}
				
				List<String> phones=new ArrayList<String>();
				//发送邮件
				for (Record deal : dealList) {
					//发送邮件、短信线程
					AlertKit alertKit=new AlertKit();
					//获取邮件内容
					String body=AlertKit.getMailBody(userinfo,deal,order).toString();
					//发送邮件通知
					if(ValidateKit.isEmail(deal.getStr("uemail"))){
						alertKit.setEmailTitle("【超时通知】故障申报超时提醒！").setEmailBody(body).setEmailAdd(deal.getStr("uemail"));
					}
					//电话&&非空 then 保存电话列表
					if(!ValidateKit.isNullOrEmpty(deal.getStr("uphone"))){
						if(ValidateKit.isPhone(deal.getStr("uphone"))){
							phones.add(deal.getStr("uphone"));
							//短信内容
							String smsBody=AlertKit.getSmsBody(userinfo,order);
							alertKit.setSmsContext(smsBody).setSmsPhone(deal.getStr("uphone"));
						}
					}
					//加入进程
					logger.info("日志添加到入库队列 ---> 超时工单催办处理");
					ThreadAlert.add(alertKit);
					renderJson("state","催办成功！");
				}
			}

		}
		
	}
	
	/**
	 * @描述 对提交故障工单进行撤回
	 */
	@Before(Tx.class)
	public void recall(){
		int id=getParaToInt("oorderid");
		Order order=Order.dao.findById(id);
		if(order.getInt("status")!=2){
			renderJson("state","工单已处理，撤回失败！");
		}else{
			order.set("deleted_at", DateKit.format(new Date(),DateKit.pattern_ymd_hms));
			
			if(order.update()){
				//当前用户详细信息
				Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
				//运维人员列表
				List<Record> dealList=new ArrayList<Record>();
				
				List<UserOrder> userOrderList=UserOrder.dao.findBy(" order_id=?", id);
				for(UserOrder userorder:userOrderList){
					Record accept_user=UserInfo.dao.getAllUserInfo(userorder.get("user_id"));
					dealList.add(accept_user);
				}
				
				List<String> phones=new ArrayList<String>();
				//发送邮件
				for (Record deal : dealList) {
					//发送邮件、短信线程
					AlertKit alertKit=new AlertKit();
					//获取邮件内容
					String body=AlertKit.getMailBodyRecall(userinfo,deal,order).toString();
					//发送邮件通知
					if(ValidateKit.isEmail(deal.getStr("uemail"))){
						alertKit.setEmailTitle("【撤回通知】故障申报撤回提醒！").setEmailBody(body).setEmailAdd(deal.getStr("uemail"));
					}
					//电话&&非空 then 保存电话列表
					if(!ValidateKit.isNullOrEmpty(deal.getStr("uphone"))){
						if(ValidateKit.isPhone(deal.getStr("uphone"))){
							phones.add(deal.getStr("uphone"));
							//短信内容
							String smsBody=AlertKit.getSmsBodyRecall(userinfo,order);
							alertKit.setSmsContext(smsBody).setSmsPhone(deal.getStr("uphone"));
						}
					}
					//加入进程
					logger.info("日志添加到入库队列 ---> 工单撤回处理通知");
					ThreadAlert.add(alertKit);
				}
				renderJson("state","撤回成功！");
			}else{
				renderJson("state","撤回失败！");
			}
		}
	}
	
	/**
	 * 删除撤回工单
	 */
	@Deprecated
	@Before(Tx.class)
	public void delete(){
		int id=getParaToInt("oorderid");
		Order order=Order.dao.findById(id);
		
		List<UserOrder> userOrderList=UserOrder.dao.findBy(" order_id=?", id);
		
		if(order.delete()){
			for(UserOrder userorder:userOrderList){
				userorder.delete();
			}
			renderJson("state","删除成功！");
		}else{
			renderJson("state","删除失败！");
		}
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
	 * @描述 新建工单，联动故障类型-相应处理人员
	 */
	public void handler(){
		String type=getPara("type");
		if(type.equals("type")){
			List<ErrorType> typeList=ErrorType.dao.getAllType();
			renderJson("typeList", typeList);
		}else if(type.equals("apartment")){
			List<Apartment> apartmentList=Apartment.dao.getApartmentsList(getParaToInt("typeid"));
			renderJson("apartmentList",apartmentList);
		}else if(type.equals("effective")){
			List<UserInfo> userinfoList=UserInfo.dao.findBy(" apartment_id=?", getParaToInt("apartmentid"));
			if(userinfoList.size()==0){
				renderJson("state","error");
			}else{
				renderJson("state","success");
			}
		}
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
				.set("title",WebKit.getHTMLToString(getPara("otitle")))
				.set("description", WebKit.getHTMLToString(getPara("odescription")))
				.set("type", getParaToInt("selectType"))
				.set("level", getPara("selectLevel"))
				.set("status", 2)
				.set("offer_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"))
				//.set("accept_user", getParaToLong("selectDeal"))
				.set("flag", 0);
		order.save();
		
		//选中部门
		long selectApartment=getParaToLong("selectApartment");
		//根据部门id，获取该部门人员
		List<Record> selectDealList=UserInfo.dao.getUserByApartment(" apartment.id=?",selectApartment);
		
		Order o=Order.dao.findById(order.get("id"));
		
		for(Record selectDeal:selectDealList){
			UserOrder userorder=new UserOrder()
			.set("user_id", selectDeal.get("userid"))
			.set("order_id", order.get("id"));
			userorder.save();
			
			//当前用户详细信息
			Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
			
			//邮件内容
			String body=AlertKit.getMailBody(userinfo,selectDeal,o,WebKit.getHTMLToString(getPara("odescription"))).toString();
			//发送邮件、短信线程
			AlertKit alertKit=new AlertKit();
			//发送邮件
			if(ValidateKit.isEmail(selectDeal.getStr("useremail"))){
				alertKit.setEmailTitle("点通故障系统提醒您！").setEmailBody(body).setEmailAdd(selectDeal.getStr("useremail"));
			}
			if(ValidateKit.isPhone(selectDeal.getStr("userphone"))){
				//短信内容
				String smsBody=AlertKit.setSmsContext(null,userinfo,o);
				alertKit.setSmsContext(smsBody).setSmsPhone(selectDeal.getStr("userphone"));
			}
			//加入进程
			logger.info("日志添加到入库队列 ---> 新建故障工单");
			ThreadAlert.add(alertKit);
		}
		
		redirect("/report/reports");
	}
	
	/**
	 * @描述 申报人员可以修改未被处理的工单
	 *            此时不会再次发送邮件、短信通知。
	 */
	@Before(CommonValidator.class)
	public void edit(){
		
		//故障类型
		setAttr("typeList",ErrorType.dao.getAllType());
		//故障等级
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
		String title=getPara("otitle");
		String description=getPara("odescription");
		
		Order order=Order.dao
				.findById(orderid);
		order.set("title", title)
				.set("description",WebKit.getHTMLToString(description))
				.set("updated_at",
						DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		
		if(order.update()){
			redirect("/report/reports");
		}

	}
	@Before(Tx.class)
	public void upup(){
		//order_id
		Integer orderid=getParaToInt("orderid");
		Order order=Order.dao.findById(orderid);
		//description
		String description=getPara("description").trim();
		String title=getPara("title").trim();
		
		if(order.getInt("status")==1){
			renderJson("state","失败：工单正在处理，无法修改。");
		}else if(order.getInt("status")==0){
			renderJson("state","失败：工单已处理，无法修改。");
		}else if(!ValidateKit.isNullOrEmpty(order.get("deleted_at"))){
			renderJson("state","失败：工单已撤销，无法修改。");
		}
		else if(ValidateKit.isNullOrEmpty(description)){
			renderJson("state","故障单描述不能为空！");
		}else if(ValidateKit.isNullOrEmpty(title)){
			renderJson("state","故障单标题不能为空！");
		}else{
			order.set("title", WebKit.getHTMLToString(title))
					.set("description",WebKit.getHTMLToString(description))
					.set("updated_at",
							DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
			
			if(order.update()){
				renderJson("state","更新故障工单成功！");
			}
			else {
				renderJson("state","操作失败！");
			}
		}
		
	}
	
	
	

}
