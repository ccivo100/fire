package com.poicom.function.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;
import cn.dreampie.shiro.core.SubjectKit;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;
import com.poicom.basic.kit.AlertKit;
import com.poicom.basic.kit.DateKit;
import com.poicom.basic.kit.StringKit;
import com.poicom.basic.kit.WebKit;
import com.poicom.basic.thread.ThreadAlert;
import com.poicom.function.model.Apartment;
import com.poicom.function.model.Comment;
import com.poicom.function.model.Etype;
import com.poicom.function.model.Level;
import com.poicom.function.model.Order;
import com.poicom.function.model.Template;
import com.poicom.function.model.User;
import com.poicom.function.model.UserInfo;
import com.poicom.function.model.UserOrder;
import com.poicom.function.service.ApartmentService;
import com.poicom.function.service.OrderService;
import com.poicom.function.service.TemplateService;
import com.poicom.function.validator.CommonValidator;
import com.poicom.function.validator.ReportValidator;

/**
 * @描述 故障申报
 * @author 唐东宇
 *
 */
@ControllerKey(value = "/report", path = "/app/report")
public class ReportController extends BaseController{
	
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
		}
		OrderService.service.format(reportPage, "title");
		setAttr("reportPage",reportPage);
		
		render("report.html");
	}
	
	/**
	 * 查询故障工单详细内容
	 */
	public void report(){
		
		Record order = Order.dao.findReportById(" o.id=? ",getParaToInt("id"));
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
	 * 获得工单运维组信息
	 */
	public void dealList(){
		Long orderid=getParaToLong("orderid");
		Map<Long,Apartment> papartmentMap=OrderService.service.getDealApartmentsByOrderid(orderid);
		Map<String,Object> jsonList= new HashMap<String,Object>();
		jsonList.put("apartments",papartmentMap);
		renderJson(jsonList);
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
	@Before({Tx.class,ReportValidator.class})
	public void recall(){
		int id=getParaToInt("order.id");
		Order order=Order.dao.findById(id);
		order.set("deleted_at", DateKit.format(new Date(),DateKit.pattern_ymd_hms));
		boolean flag=order.update();
		if(flag){
			OrderService.service.recallOrder(order);
			renderJson("state","撤回成功！");
		}else{
			renderJson("state","撤回失败！");
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
		
		setAttr("userinfo",userinfo);
		setAttr("levelList",Level.dao.findAll());
		render("add.html");
	}
	
	/**
	 * @描述 新建工单，联动故障类型-相应处理人员
	 */
	public void handler(){
		String type=getPara("type");
		if(type.equals("type")){
			List<Etype> typeList=Etype.dao.rootNode(" pid=? and deleted_at is null ", 0);
			renderJson("typeList", typeList);
		}
		
		else if(type.equals("apartment")){
			Long typeid= getParaToLong("typeid");
			Map<String,Object> jsonList= new HashMap<String,Object>();
			List<Apartment> apartmentList=Apartment.dao.getApartmentsList(typeid);
			jsonList.put("apartmentList", apartmentList);//一级部门
			
			List<Etype> childTypeList = Etype.dao.childNode(" pid=? and deleted_at is null ", typeid);
			jsonList.put("childTypeList", childTypeList);//故障子类
			renderJson(jsonList);
		}
		
		else if(type.equals("childApartment")){
			Long childTypeid= getParaToLong("childTypeid");
			Long rootApartment = getParaToLong("rootApartment[]");
			//Long rootApartment = getParaToLong("rootApartment");
			List<Apartment> childApartmentList=Apartment.dao.getATApartmentsList(rootApartment,childTypeid);
			renderJson("childApartmentList",childApartmentList);
		}
		
		else if(type.equals("mailAndSm")){
			Integer[] selectApartment = getParaValuesToInt("selectApartment[]");
			if(selectApartment.length>1){
				Long childTypeid= getParaToLong("childTypeid");
				List<User> userList = new ArrayList<User>();
				for(int i=0;i<selectApartment.length;i++){
					int rootApartment=selectApartment[i];//一级部门id
					List<Apartment> childApartmentList=Apartment.dao.getATApartmentsList(rootApartment,childTypeid);
					List<User> users = ApartmentService.service.findUsers(childApartmentList);
					userList.addAll(users);
				}
				renderJson("userList",userList);
			}else{
				Long selectChildApartment=getParaToLong("selectChildApartment");
				List<User> userList = Apartment.dao.getUsersById(selectChildApartment);
				renderJson("userList",userList);
			}
		}
		else if(type.equals("effective")){
			Long selectChildApartmentid=getParaToLong("apartmentid");
			if(selectChildApartmentid==-1){
				renderJson("state","null");
			}else{
				List<UserInfo> userinfoList=UserInfo.dao.findBy(" apartment_id=?", getParaToInt("apartmentid"));
				if(userinfoList.size()==0){
					renderJson("state","error");
				}else{
					renderJson("state","success");
				}
			}
			
		}else if(type.equals("template")){
			Long typeid= getParaToLong("typeid");
			Map<String,Object> jsonList= new HashMap<String,Object>();
			//Long childTypeid= getParaToLong("childTypeid");
			User user = SubjectKit.getUser();
			UserInfo userinfo = user.getUserInfo();
			Apartment apartment = Apartment.dao.findById(userinfo.getLong("apartment_id"));
			List<Etype> childTypeList = Etype.dao.childNode(" pid=? and deleted_at is null ", typeid);
			List<Template> templateList = TemplateService.service.findByApartmentId(apartment.getLong("pid"), typeid);
			jsonList.put("childTypeList", childTypeList);//故障子类
			jsonList.put("templateList", templateList);//模板
			renderJson(jsonList);
			
		}else if (type.equals("setValue")) {
			Long selectTemplateid = getParaToLong("selectTemplateid");
			
			Map<String,Object> jsonList= new HashMap<String,Object>();
			
			Template template = Template.dao.findById(selectTemplateid);
			jsonList.put("template", template);	//模板内容
			
			if(!ValidateKit.isNullOrEmpty(template)){
				List<User> userList = User.dao.findUserAttrValues(" * ",template.getStr("receive_userids"));
				jsonList.put("userList", userList);	//接收用户
			}
			renderJson(jsonList);
		}
	}
	
	
	/**
	 * @描述 新建故障工单 并发送邮件、短信通知
	 */
	@Before({ReportValidator.class,Tx.class})
	public void save(){
		Order order = getModel(Order.class);
		boolean flag = OrderService.service.saveOrder(order);		//先提交工单，成功后建立关联并发送通知。
		if(flag){
			Integer[] selectApartment;
			String[] selectMailes, selectSmses;
			String selectMail, selectSms;
			selectApartment=getParaValuesToInt("selectApartment[]");
			selectMailes = getParaValues("selectMail[]");
			selectSmses = getParaValues("selectSms[]");
			
			//Integer selectApartment=getParaToInt("selectApartment");
			if(selectApartment.length==1){    //当一级部门只有一个时
			long selectChildApartment=getParaToLong("selectChildApartment");    //选中部门
				OrderService.service.saveUserOrderToOwnApart(order);
				OrderService.service.saveUserOrder(selectChildApartment, order);
				renderJson("state","提交成功！");
			}else if(selectApartment.length>1){    //当一级部门为多个时。
				Long childTypeid= getParaToLong("childTypeid");
				OrderService.service.saveUserOrderToOwnApart(order);
				for(int i=0;i<selectApartment.length;i++){
					int rootApartment=selectApartment[i];    //一级部门id
					List<Apartment> childApartmentList=Apartment.dao.getATApartmentsList(rootApartment,childTypeid);    //根据故障类型，获得二级部门
					for(Apartment childApartment:childApartmentList){
						OrderService.service.saveUserOrder(childApartment.getLong("id"), order);
					}
				}
				renderJson("state","提交成功！");
			}
			
			if(!ValidateKit.isNullOrEmpty(selectMailes) ){
				selectMail = StringKit.arrayWithDot(selectMailes);    //  将提交的id转换为  xx,xx,xx
				OrderService.service.sendMail(selectMail, order);		//发送邮件提醒
			}
			if(!ValidateKit.isNullOrEmpty(selectSmses)){
				selectSms = StringKit.arrayWithDot(selectSmses);
				OrderService.service.sendSms(selectSms, order);		//发送短信提醒
			}
			
		}else{
			renderJson("state","提交失败！");
		}
	}
	
	
	/**
	 * @描述 申报人员可以修改未被处理的工单
	 *            此时不会再次发送邮件、短信通知。
	 */
	@Before(CommonValidator.class)
	public void edit(){
		
		//故障类型
		setAttr("typeList",Etype.dao.getAllType());
		//故障等级
		setAttr("levelList",Level.dao.findAll());
		
		String where="o.id=?";
		//工单详细信息
		Record order = Order.dao.findReportById(where,getParaToInt("id"));
		setAttr(order);
		setAttr("order", order);
		
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		setAttr("userinfo",userinfo);
		
	}
	
	/**
	 *  @描述 处理申报人员更新故障工单操作 
	 */
	@Before({ReportValidator.class,Tx.class})
	public void update(){
		Order order=getModel(Order.class);
		boolean flag = OrderService.service.updateOrder(order);
		if(flag){
			renderJson("state","更新故障工单成功！");
		}
		else {
			renderJson("state","操作失败！");
		}
		
	}
	
	/**
	 * 模板保存工单
	 */
	@Before({ReportValidator.class,Tx.class})
	public void saveByTemplate(){
		Order order = getModel(Order.class);
		order.set("level", 1);
		Template template = Template.dao.findById(getParaToLong("selectTemplate"));
		List<User> userList = User.dao.findUserAttrValues(" * ",template.getStr("receive_userids"));
		
		//  提交工单内容
		boolean flag = OrderService.service.saveOrder(order);
		if(flag){
			
			//  通知自己部门人员
			OrderService.service.saveUserOrderToOwnApart(order);
			//  提交工单关联
			OrderService.service.saveUserOrderByTemplate(userList, order);
			OrderService.service.sendMailAndSmsByTemplate(userList, order);
			renderJson("state","提交成功！");
		}else {
			renderJson("state","操作失败！");
		}
		
	}
	
	

}
