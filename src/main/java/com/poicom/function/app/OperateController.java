package com.poicom.function.app;

import java.util.ArrayList;
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
import com.poicom.common.kit.WebKit;
import com.poicom.common.thread.ThreadAlert;
import com.poicom.function.app.model.Comment;
import com.poicom.function.app.model.ErrorType;
import com.poicom.function.app.model.Level;
import com.poicom.function.app.model.Order;
import com.poicom.function.app.model.UserOrder;
import com.poicom.function.system.model.User;
import com.poicom.function.system.model.UserInfo;

/**
 * @描述 运维管理
 * @author 唐东宇
 *
 */
@ControllerKey(value="/operate",path="/app/operate")
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
		//String condition;
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
			
			String where=" 1=1 and o.deleted_at is null and o.status=0 and o.id IN(SELECT userorder.order_id  FROM com_user_order AS userorder WHERE user_id=?)  ";
			String orderby=" ORDER BY o.offer_at DESC ";
			operatePage=Order.dao.findOperatesByUserId(getParaToInt(0,1), 10,where,orderby, user.get("id"));
			Order.dao.format(operatePage,"title");
			setAttr("operatePage",operatePage);
			
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
			String where=" 1=1 and o.deleted_at is null and o.status=0 and o.id IN(SELECT userorder.order_id  FROM com_user_order AS userorder WHERE user_id=?)  "+whereadd.toString();
			String orderby=" ORDER BY o.offer_at DESC ";
			Object[] condition= new Object[conditions.size()];
			conditions.toArray(condition);
			operatePage=Order.dao.findOperatesByUserId(getParaToInt(0,1), 10,where,orderby,condition);
			Order.dao.format(operatePage,"title");
			setAttr("operatePage",operatePage);
			
		}

		render(OPERATE_PAGE);
		
	}
	
	
	public void operate(){
		
		//工单详细信息
		String where="o.id=?";
		Record order = Order.dao.findOperateById(where,getParaToInt("id"));
		setAttr(order);
		
		//获取工单申报者的分公司信息
		Record offer=UserInfo.dao.getUserBranch(order.getLong("oofferid"));
		
		//获取工单处理意见
		List<Record> commentList=Comment.dao.findCommentsByOrderId(" comments.order_id=? order by comments.add_at asc ",order.getLong("oorderid"));
		setAttr("commentList",commentList);
		
		if(!ValidateKit.isNullOrEmpty(offer)){
			setAttr("offer",offer);
		}
		render(OPERATE_QUERY_PAGE);
	}
	
	/**
	 * @描述 进入故障工单处理页面
	 */
	@Before({Tx.class,CommonValidator.class})
	public void edit(){
		
		//工单详细信息
		String where="o.id=?";
		Record order = Order.dao.findOperateById(where,getParaToInt("id"));
		setAttr(order);
		
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		setAttr(userinfo);
		
		//获取工单申报者的单位、部门、职位信息
		Record offer=UserInfo.dao.getUserBranch(order.getLong("oofferid"));

		if(!ValidateKit.isNullOrEmpty(offer)){
			setAttr("offer",offer);
		}
		
		List<Record> commentList=Comment.dao.findCommentsByOrderId(" comments.order_id=? order by comments.add_at asc ",order.getLong("oorderid"));
		setAttr("commentList",commentList);
		
		render(OPERATE_EDIT_PAGE);
		
	}
	
	/**
	 * 运维主管下发清单
	 */
	@Deprecated
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
				
				//发送邮件、短信线程
				AlertKit alertKit=new AlertKit();
				//申报人员信息
				Record offeruser=UserInfo.dao.getAllUserInfo(order.get("offer_user"));
				//受理人员信息
				Record acceptuser=UserInfo.dao.getAllUserInfo(order.get("accept_user"));
				//处理人员信息
				Record dealuser=UserInfo.dao.getAllUserInfo(order.get("deal_user"));
				//获取邮件内容
				String body=AlertKit.getMailBodyArrange(offeruser,acceptuser,dealuser,order).toString();
				//发送邮件通知
				if(ValidateKit.isEmail(offeruser.getStr("uemail"))){
					alertKit.setEmailTitle("【指派通知】故障申报人员指派通知！").setEmailBody(body).setEmailAdd(offeruser.getStr("uemail"));
				}
				if(ValidateKit.isPhone(offeruser.getStr("uphone"))){
					//短信内容
					String smsBody=AlertKit.getSmsBodyArrange(offeruser,acceptuser,dealuser,order);
					alertKit.setSmsContext(smsBody).setSmsPhone(offeruser.getStr("uphone"));
				}
				//加入进程
				logger.info("日志添加到入库队列 ---> 指派处理人员");
				ThreadAlert.add(alertKit);
				
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
		//处理进度
		int selectProgress=getParaToInt("progress");
		
		Order order=Order.dao.findById(orderid);
		User cUser=SubjectKit.getUser();
		if(!ValidateKit.isDateTime(deal_at)){
			renderJson("state","请选择处理时间！");
		}else if(selectProgress==-1){
			renderJson("state","请选择处理进度！");
		}
		else if(ValidateKit.isNullOrEmpty(comment))
		{
			renderJson("state","处理意见不为空！");
		}
		else if(!ValidateKit.isNullOrEmpty(order.get("deleted_at"))){
			renderJson("state","失败：工单已撤销，无法提交。");
		}
		else{
			
			// 设置基本内容：处理人，建议，处理时间，修改状态等。
			Comment com=new Comment();
			com.set("order_id", orderid)
					.set("user_id", cUser.get("id"))
					.set("context", WebKit.getHTMLToString(comment))
					.set("add_at", deal_at)
					.set("created_at",
							DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
			com.save();

			//开始处理
			if(selectProgress==0){
				//设置状态为 1 及处理中
				order.set("status", 1).update();
				renderJson("state","故障工单开始处理！");
			}
			//继续处理
			else if(selectProgress==1){
				renderJson("state","故障工单继续处理！");
			}
			//处理完毕
			else if(selectProgress==2){
				//设置状态为 0 及已处理
				order.set("status", 0);
				
				int time = Level.dao.findById(order.get("level")).get("deadline");
				String offer_at = order.get("offer_at").toString();
				DateTime now = DateTime.now();
				int t = DateKit.dateBetween(offer_at, now);
				
				// 若处理时间超时，则
				if (t > time) {
					order.set("spend_time", t);
				}
				if (order.update()) {
					// 故障工单提交人员
					User offer = User.dao.findById(order.get("offer_user"));
					// 获取邮件内容
					String body = AlertKit.getMailBody(offer, cUser, order)
							.toString();
					AlertKit alertKit = new AlertKit();
					if (ValidateKit.isEmail(offer.getStr("email"))) {
						alertKit.setEmailTitle("故障申报处理情况通知！").setEmailBody(body)
								.setEmailAdd(offer.getStr("email"));
					}
					// 电话非空
					if (!ValidateKit.isNullOrEmpty(offer.getStr("phone"))) {
						if (ValidateKit.isPhone(offer.getStr("phone"))) {
							// 短信内容
							String smsBody = AlertKit
									.getSmsBody(offer, cUser, order);
							alertKit.setSmsContext(smsBody).setSmsPhone(
									offer.getStr("phone"));
						}
					}
					// 加入进程
					logger.info("日志添加到入库队列 ---> 处理故障工单");
					ThreadAlert.add(alertKit);
					renderJson("state", "故障工单处理完毕！");
				} else {
					renderJson("state", "提交失败！");
				}
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
	@Deprecated
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
	@Deprecated
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
