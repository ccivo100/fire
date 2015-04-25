package com.poicom.function.app;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;
import cn.dreampie.shiro.core.SubjectKit;

import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
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
import com.poicom.function.app.model.UserOrder;
import com.poicom.function.system.model.User;
import com.poicom.function.system.model.UserInfo;

/**
 * @描述 运维管理
 * @author poicom7
 *
 */
@ControllerKey(value="/operate",path="/page/app/operate")
public class OperateController extends JFController{
	protected Logger logger=LoggerFactory.getLogger(getClass());
	
	private final static String OPERATE_PAGE="operate.html";
	private final static String DEAL_PAGE="deal.html";
	private final static String OPERATE_EDIT_PAGE="edit.html";
	private final static String OPERATE_QUERY_PAGE="query.html";
	private final static String ASSIGN_PAGE="assign.ftl";
	private final static String OPERATE_TASK_PAGE="task.ftl";
	
	public void index(){
		
	}
	
	/**
	 * @描述 运维人员查询本账号处理范围内的故障工单，用于故障查询
	 */
	public void operates(){
		User user=User.dao.getCurrentUser();
		Page <Record> operatePage;
		if(ValidateKit.isNullOrEmpty(user)){
			render("operate.html");
		}else{
			String where=" 1=1 and o.deleted_at is null and o.id IN(SELECT userorder.order_id  FROM com_user_order AS userorder WHERE user_id=?) ";
			String orderby=" ORDER BY o.offer_at DESC ";
			operatePage=Order.dao.findOperatesByUserId(getParaToInt(0,1), 10,where,orderby, user.get("id"));
			Order.dao.format(operatePage,"title");
			setAttr("operatePage",operatePage);
			render(OPERATE_PAGE);
		}
		
	}
	
	public void testoperates(){
		User user=User.dao.getCurrentUser();
		List <Record> operateList;
		if(ValidateKit.isNullOrEmpty(user)){
			renderJson("operate.html");
		}else{
			String where=" 1=1 and o.deleted_at is null and o.id IN(SELECT userorder.order_id  FROM com_user_order AS userorder WHERE user_id=?) ";
			String orderby=" ORDER BY o.offer_at DESC ";
			operateList=Order.dao.findOperatesByUserId(where,orderby, user.get("id"));
			Order.dao.format(operateList,"title");
			renderJson(operateList);
			
		}
	}
	
	public void operate(){
		
		Order o=Order.dao.findById(getParaToInt("id"));
		//受理人第一次查询时，插入受理人处理时间
		if(ValidateKit.isNullOrEmpty(o.get("accepted_at"))){
			o.set("accepted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		}
		//如果受理人和当前用户不是同一人，同一人则只有一个受理时间accepted_at。
		if(!o.getLong("accept_user").equals(SubjectKit.getUser().getLong("id"))){
			//处理人第一次查询时，插入处理人受理时间
			if(ValidateKit.isNullOrEmpty(o.get("accept_at"))){
				o.set("accept_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
			}
		}
		
		String where="o.id=?";
		Record order = Order.dao.findOperateById(where,getParaToInt("id"));

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
		
		render(OPERATE_QUERY_PAGE);
	}
	
	/**
	 * @描述 进入故障工单处理页面
	 */
	@Before({Tx.class,CommonValidator.class})
	public void edit(){
		//故障类型
		setAttr("typeList",ErrorType.dao.getAllType());
		//
		setAttr("levelList",Level.dao.findAll());
		
		String where="o.id=?";
		Order o=Order.dao.findById(getParaToInt("id"));
		//受理人第一次点击处理时，插入受理人处理时间
		if(ValidateKit.isNullOrEmpty(o.get("accepted_at"))){
			o.set("accepted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		}
		//如果受理人和当前用户不是同一人，同一人则只有一个受理时间accepted_at。
		if(!o.getLong("accept_user").equals(SubjectKit.getUser().getLong("id"))){
			if(ValidateKit.isNullOrEmpty(o.get("accept_at"))){
				//处理人第一次点击处理时，插入处理人受理时间
				o.set("accept_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
			}
		}
		//工单详细信息
		Record order = Order.dao.findOperateById(where,getParaToInt("id"));
		setAttr(order);
		//setAttr("order", order);
		
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		setAttr(userinfo);
		//setAttr("userinfo",userinfo);
		
		//获取工单申报者的单位、部门、职位信息
		Record offer=UserInfo.dao.getUserBranch(order.getLong("oofferid"));

		if(!ValidateKit.isNullOrEmpty(offer)){
			setAttr("offer_branch",offer.getStr("bname"));
			setAttr("offer_apartment",offer.getStr("aname"));
			setAttr("offer_position",offer.getStr("pname"));
		}
		
		//除当前用户以外的所有运维人员
		where=" userinfo.apartment_id=? and user.id<>?";
		List<Record> dealList=UserInfo.dao.getUserByApartment(where,2,User.dao.getCurrentUser().get("id"));
		setAttr("dealList",dealList);
		
		render(OPERATE_EDIT_PAGE);
		
	}
	
	/**
	 * 运维主管下发清单
	 */
	public void arrange(){
		int dealid=getParaToInt("selectDeal");
		int oorderid=getParaToInt("oorderid");
		
		Order order=Order.dao.findById(oorderid);
		UserOrder userorder=UserOrder.dao.findFirstBy(" order_id=?", oorderid);
		if(dealid==-1){
			renderJson("state","请选择指派人员");
		}
		else if(ValidateKit.isNullOrEmpty(order.get("deal_user"))){
			userorder.set("user_id", dealid);
			if(userorder.update()){
				order.set("deal_user", dealid).update();
				renderJson("state","指派成功");
			}else{
				renderJson("state","指派失败");
			}
		}
		else
			renderJson("state","指派失败");
	} 
	
	/**
	 * @描述 提交故障处理建议
	 */
	@Before({Tx.class,EvictInterceptor.class})
	@CacheName("/order/query")
	public void update(){
		//order_id
		Integer orderid=getParaToInt("oorderid");
		//comment
		String comment=getPara("ocomment");
		//deal_at
		String deal_at=getPara("deal_at");
		
		Order order=Order.dao.findById(orderid);
		User cUser=SubjectKit.getUser();
		if(!ValidateKit.isDateTime(deal_at)){
			renderJson("state","请选择处理时间！");
		}else if(DateKit.isDateBefore(deal_at, DateTime.now().toString("yyyy-MM-dd HH:mm:ss"))){
			renderJson("state","所选时间不可早于当前时间！");
		}
		else if(!ValidateKit.isLength(comment, 15, 250))
		{
			renderJson("state","处理意见应不少于15字！");
		}else if(!ValidateKit.isNullOrEmpty(order.getLong("deal_user"))){
			if(!ValidateKit.isNullOrEmpty(order.get("comment"))){
				renderJson("state","失败：工单已提交");
			}else if(cUser.getLong("id").equals(order.getLong("accept_user"))){
				renderJson("state","失败：工单已指派其他运维员处理");
			}
		}else{

			int time=Level.dao.findById(order.get("level")).get("deadline");
			String offer_at=order.get("offer_at").toString();
			DateTime now=DateTime.now();
			int t =DateKit.dateBetween(offer_at, now);
			
			//设置基本内容：处理人，建议，处理时间，修改状态等。
			if(ValidateKit.isNullOrEmpty(order.get("deal_user"))){
				order.set("deal_user", getParaToInt("uuserid"));
			}
			order
			.set("comment", comment)
			.set("deal_at", deal_at)
			.set("status", 0);
			
			//若处理时间超时，则
			if(t>time){
				order.set("spend_time", t);
			}
			
			if(order.update()){
				//故障工单提交人员
				User offer=User.dao.findById(order.get("offer_user"));
				User deal=User.dao.findById(order.get("deal_user"));
				//获取邮件内容
				String body=AlertKit.getMailBody(offer,deal,order).toString();
				if(ValidateKit.isEmail(offer.getStr("email"))){
					AlertKit.sendEmail("故障申报处理情况通知！",body,offer.getStr("email"));
				}
				//电话非空 
				if(!ValidateKit.isNullOrEmpty(offer.getStr("phone"))){
					if(ValidateKit.isPhone(offer.getStr("phone"))){
						//发送短信
						//AlertKit.sendSms(offer,deal,order);
					}
				}
				renderJson("state","提交成功！");
			}else{
				renderJson("state","提交失败！");
			}
		}
	}
	
	/**
	 * 添加补充意见
	 */
	public void addupdate(){
		//order_id
		Integer orderid = getParaToInt("oorderid");
		// addcomment
		String addcomment = getPara("addcomment");
		//add_at
		String add_at = getPara("add_at");

		Order order = Order.dao.findById(orderid);
		User cUser = SubjectKit.getUser();
		
		String deal_at=order.get("deal_at").toString();
		DateTime now=DateTime.now();
		int t =DateKit.dateBetween(deal_at, now);
		if(t>72){
			renderJson("state", "该故障单时间过去太久，不提供补充意见功能！");
		}
		else if (!ValidateKit.isDateTime(add_at)) {
			renderJson("state", "请选择处理时间！");
		} else if (DateKit.isDateBefore(add_at,
				DateTime.now().toString("yyyy-MM-dd HH:mm:ss"))) {
			renderJson("state", "所选时间不可早于当前时间！");
		} else if (!ValidateKit.isLength(addcomment, 15, 250)) {
			renderJson("state", "处理意见应不少于15字！");
		}else if(!ValidateKit.isNullOrEmpty(order.get("addcomment"))){
				renderJson("state","失败：工单已提交");
		
		}else{
			order
				.set("addcomment", addcomment)
				.set("add_at", add_at);
			
			if(order.update()){
				//故障工单提交人员
				User offer=User.dao.findById(order.get("offer_user"));
				User deal=User.dao.findById(order.get("deal_user"));
				//获取邮件内容
				//String body=AlertKit.getMailBody(offer,deal,order).toString();
				if(ValidateKit.isEmail(offer.getStr("email"))){
					//AlertKit.sendEmail("故障申报补充处理情况通知！",body,offer.getStr("email"));
				}
				//电话非空 
				if(!ValidateKit.isNullOrEmpty(offer.getStr("phone"))){
					if(ValidateKit.isPhone(offer.getStr("phone"))){
						//发送短信
						//AlertKit.sendSms(offer,deal,order);
					}
				}
				
				renderJson("state","提交成功！");
			}else{
				renderJson("state","提交失败！");
			}
			
		}
		
		
		
	}
	
	/**
	 * @描述 运维人员查询本账号处理范围内的故障工单，用于故障处理
	 */
	public void deal(){
		User user=User.dao.getCurrentUser();
		if(ValidateKit.isNullOrEmpty(user)){
			render("operate.html");
		}else{
			String where=" 1=1 and o.deleted_at is null and o.status<>0 and o.id IN(SELECT userorder.order_id  FROM com_user_order AS userorder WHERE user_id=?) ";
			String orderby=" ORDER BY o.offer_at DESC ";
			Page <Record> operatePage=Order.dao.findOperatesByUserId(getParaToInt(0,1), 10,where,orderby, user.get("id"));
			Order.dao.format(operatePage,"description");
			setAttr("operatePage",operatePage);
			render(DEAL_PAGE);
		}
	}
	
	public void addComment(){
		
	}
	
	
	/**
	 * @描述 任务分配
	 */
	public void task(){
		//Branch_id 为10的用户。
		String where=" userinfo.apartment_id=?";
		
		Page<Record> userPage=UserInfo.dao.getUserByBranch(getParaToInt(0,1), 10,where,2);
		
		for(int i=0;i<userPage.getList().size();i++){
			List<Record> list= ErrorType.dao.getOperatorType(userPage.getList().get(i).get("userid"));
			StringBuffer types=new StringBuffer();
			for(int j=0;j<list.size();j++){
				types.append(list.get(j).getStr("typename")+"；");
			}
			//userPage.getList().get(i).set("remark", types.toString());
			userPage.getList().get(i).set("remark", list);
		}
		
		setAttr("userPage",userPage);
		setAttr("typeList",ErrorType.dao.getAllType());
		
		render(OPERATE_TASK_PAGE);
	}
	
	/**
	 * @描述 分配操作
	 */
	public void assign(){
		String userid=getPara("id");
		setAttr("userinfo",UserInfo.dao.getAllUserInfo(userid));
		setAttr("usertype",ErrorType.dao.getOperatorType(userid));
		setAttr("typeList",ErrorType.dao.getAllType());
		render(ASSIGN_PAGE);
	}
	
	/**
	 * @描述 执行分配操作
	 */
	@Before(Tx.class)
	public void doassign(){
		//前台选中的 类型s
		String[] types=getParaValues("types");
		
		//用户-类型 中间表
		List<Record> cut=ErrorType.dao.getTypeByUser(getParaToLong("userid"));
		
		
		if(ValidateKit.isNullOrEmpty(types)){
			for(int i=0;i<cut.size();i++){
				System.out.println(cut.get(i).getLong("typeid")+" 已取消，执行删除操作！");
				Db.deleteById("com_user_type", cut.get(i).getLong("id"));
			}
		}else if(ValidateKit.isNullOrEmpty(cut)){
			for(int i=0;i<types.length;i++){
				System.out.println(types[i]+" 不存在，执行新增操作！");
				Record record=new Record().set("user_id", getPara("userid")).set("type_id", types[i]);
				Db.save("com_user_type", record);
			}
		}else{
			//需要处理该故障类型?，不存在则新增。
			for(int i=0;i<types.length;i++){
				boolean flag=false;
				for(int j=0;j<cut.size();j++){
					if(Integer.parseInt(types[i])==cut.get(j).getLong("typeid")){
						flag=true;
						break;
					}
				}
				if(flag){
					System.out.println(types[i]+" 存在，保留不删除！");
				}else if(!flag){
					System.out.println(types[i]+" 不存在，执行新增操作！");
					Record record=new Record().set("user_id", getPara("userid")).set("type_id", types[i]);
					Db.save("com_user_type", record);
				}
			}
			
			//需要保留该故障类型?，不保留则删除
			for(int i=0;i<cut.size();i++){
				boolean flag=false;
				for(int j=0;j<types.length;j++){
					if(cut.get(i).getLong("typeid")==Integer.parseInt(types[j])){
						flag=true;
						break;
					}
				}
				if(flag){
					System.out.println(cut.get(i).getLong("typeid")+" 未取消，保留不删除！");
				}else if(!flag){
					System.out.println(cut.get(i).getLong("typeid")+" 已取消，执行删除操作！");
					Db.deleteById("com_user_type", cut.get(i).getLong("id"));
				}
			}
		}
		
		redirect("/operate/task");
	}


}
