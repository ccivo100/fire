package com.poicom.function.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import cn.dreampie.ValidateKit;
import cn.dreampie.shiro.core.SubjectKit;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.poicom.basic.kit.AlertKit;
import com.poicom.basic.kit.WebKit;
import com.poicom.basic.thread.ThreadAlert;
import com.poicom.function.model.Apartment;
import com.poicom.function.model.Comment;
import com.poicom.function.model.Etype;
import com.poicom.function.model.Order;
import com.poicom.function.model.User;
import com.poicom.function.model.UserInfo;
import com.poicom.function.model.UserOrder;
import com.poicom.function.notice.MailSender;
import com.poicom.function.notice.Notice;
import com.poicom.function.notice.Provider;
import com.poicom.function.notice.Sender;
import com.poicom.function.notice.SmsSender;
import com.poicom.function.notice.factory.SendMailFactory;
import com.poicom.function.notice.factory.SendSmsFactory;
import com.poicom.function.notice.template.MailTemplate;
import com.poicom.function.notice.template.SmsTemplate;

public class OrderService extends BaseService {
	
	private static Logger log = Logger.getLogger(OrderService.class);
	
	public static OrderService service = new OrderService();
	
	private MailSender mailSender;
	private SmsSender smsSender;
	
	public OrderService(){
		mailSender = (MailSender) new SendMailFactory().produce();
		smsSender = (SmsSender) new SendSmsFactory().produce();
	}
	
	private List<String> recipients = new ArrayList<String>();
	private List<String> phones = new ArrayList<String>();
	
	public void format(Page<Record> page,String... paras){
		for(Record record:page.getList()){
			for(String attr:paras) {
				record.set(attr, StringUtils.abbreviate(record.getStr(attr), (new Random().nextInt(2)+35)));
			}
			Long orderid=record.getLong("id");
			UserOrder userorder = UserOrder.dao.findFirstBy(" order_id=? ", orderid);
			Long userid = userorder.getLong("user_id");
			User user= User.dao.findById(userid);
			String paname=user.getPosition().getStr("apartment_name");
			record.set("deal_paname", paname);
		}
	}
	
	/**
	 * 根据工单故障类型id，查其父类pid。排除已指派部门。
	 * @param typeid 工单故障子类型id
	 * @return 该工单未指派根部门list
	 */
	public List<Apartment> apartmentByTypeExcApart(Long orderid,Long typeid){
		Set<Long> apidSet=new HashSet<Long>();
		
		List<User> userList = UserOrder.dao.getUserList(orderid);//根据工单id，查询工单拥有者
		for(User user:userList){
			UserInfo userinfo = UserInfo.dao.get("user_id",user.get("id"));//每个拥有者的详细信息
			Long apartment_id = userinfo.getLong("apartment_id");//拥有者的部门id（二级部门）
			Apartment apartment = Apartment.dao.findById(apartment_id);//二级部门信息
			Long apartment_pid=apartment.getLong("pid");//父级部门
			apidSet.add(apartment_pid);
		}
		
		Long typepid = Etype.dao.findById(typeid).getLong("pid");//根据细类id获得父类id
		Long[] apids=apidSet.toArray(new Long[apidSet.size()]);
		Long[] l1={typepid};
		int lsize1=l1.length;
		int lsize2=apids.length;
		
		l1= Arrays.copyOf(l1,lsize1+ lsize2);//扩容
		System.arraycopy(apids, 0, l1, lsize1,lsize2 );//将第二个数组与第一个数组合并
		
		List<Apartment> apartmentList=Apartment.dao.getApartmentsListExcept(l1);
		return apartmentList;
	}
	
	/**
	 * 根据工单id，查询需要处理工单人员的一级部门，和二级部门。
	 * 二级部门为一级部门对象的child属性。
	 * @param orderid
	 * @return papartmentMap 
	 */
	public Map<Long,Apartment> getDealApartmentsByOrderid(Long orderid){
		List<User> userList = UserOrder.dao.getUserList(orderid);//根据工单id，查询工单拥有者
		Map<Long,Apartment> papartmentMap=new HashMap<Long,Apartment>();//保存父级部门。
		for(User user:userList){
			UserInfo userinfo = UserInfo.dao.get("user_id",user.get("id"));//每个拥有者的详细信息
			Long apartment_id = userinfo.getLong("apartment_id");//拥有者的部门id（二级部门）
			Apartment apartment = Apartment.dao.findById(apartment_id);//二级部门信息
			Long papartment_id=apartment.getLong("pid");//父级部门id
			Apartment papartment;
			//在获得父级部门后，添加入
			if(papartmentMap.get(papartment_id)==null){
				papartment= Apartment.dao.findById(papartment_id);//父级部门信息
				Map<Long,Apartment> childList;
				if(papartment.getChild()==null){
					childList=new HashMap<Long,Apartment>();
				}else{
					childList=papartment.getChild();
				}
				childList.put(apartment_id,apartment);
				papartment.setChild(childList);
				papartmentMap.put(papartment_id, papartment);
			}else{
				papartment =papartmentMap.get(papartment_id);
				Map<Long,Apartment> childList;
				if(papartment.getChild()==null){
					childList=new HashMap<Long,Apartment>();
				}else{
					childList=papartment.getChild();
				}
				childList.put(apartment_id,apartment);
				papartment.setChild(childList);
			}
		}
		return papartmentMap;
	}
	
	/**
	 * 根据工单id获取处理人员的二级部门信息。
	 * @param orderid
	 * @return papartmentMap
	 */
	public Map<Long,Apartment> getDealCApartmentsByOrderid(Long orderid){
		List<User> userList = UserOrder.dao.getUserList(orderid);//根据工单id，查询工单拥有者
		Map<Long,Apartment> papartmentMap=new HashMap<Long,Apartment>();//保存父级部门。
		for(User user:userList){
			UserInfo userinfo = UserInfo.dao.get("user_id",user.get("id"));//每个拥有者的详细信息
			Long apartment_id = userinfo.getLong("apartment_id");//拥有者的部门id（二级部门）
			Apartment apartment = Apartment.dao.findById(apartment_id);//二级部门信息
			papartmentMap.put(apartment_id, apartment);
		}
		return papartmentMap;
	}
	
	public Record query(Long orderid){
		Record order = Order.dao.query(" o.id=?", orderid);
		return order;
	}
	
	public boolean updateOrder(Order order){
		order
			.set("title", WebKit.getHTMLToString(order.getStr("title")))
			.set("description",WebKit.getHTMLToString(order.getStr("description")))
			.set("updated_at",
				DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		return order.update();
	}
	
	
	
	/**
	 * 新增工单
	 * @param order
	 * @return
	 */
	public boolean saveOrder(Order order){
		order
			.set("title", WebKit.getHTMLToString(order.getStr("title")))
			.set("description", WebKit.getHTMLToString(order.getStr("description")))
			.set("status", 2)
			.set("offer_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"))
			.set("flag", 0);
		return order.save();
	}
	
	/**
	 * 新增工单回复，处理人，处理意见，处理时间，修改状态等。
	 * @param comment
	 * @return
	 */
	public boolean saveComment(Comment comment,Long orderid,Long userid){
		comment.set("order_id", orderid).set("user_id", userid).set("created_at",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		return comment.save();
	}
	
	
	
	/**
	 * 提交工单部门人员消息提醒
	 * @param order
	 */
	public void saveUserOrderToOwnApart(Order order){
		User user = User.dao.getCurrentUser();//当前用户
		Record userinfo=UserInfo.dao.getAllUserInfo(user.getLong("id"));//当前用户详细信息
		List<Record> receiverList=UserService.service.userinfosByApartment(user);
		
		for(Record receiver:receiverList){
			recipients.add(receiver.getStr("useremail"));
			phones.add(receiver.getStr("userphone"));
		}
		String[] recipient = recipients.toArray(new String[recipients.size()]);
		String[] phone = phones.toArray(new String[phones.size()]);
		Notice.newOrderToOwn(mailSender, smsSender, userinfo, order, recipient, phone);
		recipients.clear();
		phones.clear();
	}
	public void saveUserOrderToOwnApart2(Order order){
		User user = User.dao.getCurrentUser();//当前用户
		Record userinfo=UserInfo.dao.getAllUserInfo(user.getLong("id"));//当前用户详细信息
		List<Record> receiverList=UserService.service.userinfosByApartment(user);
		
		for(Record receiver:receiverList){
			recipients.add(receiver.getStr("useremail"));
			//phones.add(receiver.getStr("userphone"));
		}
		String[] recipient = recipients.toArray(new String[recipients.size()]);
		String[] phone = phones.toArray(new String[phones.size()]);
		Notice.newOrderToOwn(mailSender, smsSender, userinfo, order, recipient, phone);
		recipients.clear();
		phones.clear();
	}
	/**
	 * 提交工单处理人员
	 * @param apartmentid
	 * @param order
	 */
	public void saveUserOrder(Long apartmentid,Order order){
		//根据部门id，获取该部门人员
		List<Record> selectDealList=UserInfo.dao.getUserByApartment(" apartment.id=? and user.deleted_at is null",apartmentid);
		
		for(Record selectDeal:selectDealList){
			UserOrder userorder=new UserOrder()
				.set("user_id", selectDeal.get("userid"))
				.set("order_id", order.get("id"));
			userorder.save();
		}
	}
	

	
	/**
	 * 提交工单处理人员消息提醒，单纯发送邮件、短信。配合saveUserOrder方法。
	 * @param selectMail
	 * @param selectSms
	 * @param order
	 */
/*	public void sendMailAndSms(String selectMail, String selectSms, Order order){
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		List<User> mailUserList = User.dao.findUserAttrValues("email", selectMail);
		List<User> smsUserList = User.dao.findUserAttrValues("phone", selectSms);
		for(User mailUser : mailUserList){
			recipients.add(mailUser.getStr("email"));   //只有email
		}
		for(User smsUser : smsUserList){
			phones.add(smsUser.getStr("phone"));    //只有phone
		}
		String[] recipient = recipients.toArray(new String[recipients.size()]);
		String[] phone = phones.toArray(new String[phones.size()]);
		Notice.newOrder(mailSender, smsSender, userinfo, order, recipient, phone);
		recipients.clear();
		phones.clear();
	}*/
	
	/**
	 * 提交工单处理人员消息提醒，单纯发送邮件。配合saveUserOrder方法。
	 * @param selectMail
	 * @param order
	 */
	public void sendMail(String selectMail, Order order){
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		List<User> mailUserList = User.dao.findUserAttrValues("email", selectMail);
		for(User mailUser : mailUserList){
			recipients.add(mailUser.getStr("email"));   //只有email
		}
		String[] recipient = recipients.toArray(new String[recipients.size()]);
		Notice.newOrder(mailSender, userinfo, order, recipient);
		recipients.clear();
	}
	
	/**
	 * 提交工单处理人员消息提醒，单纯发送短信。配合saveUserOrder方法。
	 * @param selectSms
	 * @param order
	 */
	public void sendSms(String selectSms, Order order){
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		List<User> smsUserList = User.dao.findUserAttrValues("phone", selectSms);
		for(User smsUser : smsUserList){
			phones.add(smsUser.getStr("phone"));    //只有phone
		}
		String[] phone = phones.toArray(new String[phones.size()]);
		Notice.newOrder(smsSender, userinfo, order, phone);
		phones.clear();
	}
	
	/**
	 * 提交工单处理人员-通过模板
	 * @param userList
	 * @param order
	 */
	public void saveUserOrderByTemplate(List<User> userList, Order order){
		for(User user: userList){
			UserOrder userorder=new UserOrder()
			.set("user_id", user.get("id"))
			.set("order_id", order.get("id"));
			userorder.save();
		}
	}
	
	/**
	 * 提交工单处理人员消息提醒，单纯发送邮件、短信。配合saveUserOrderByTemplate方法。
	 * @param userList
	 * @param order
	 */
	public void sendMailAndSmsByTemplate(List<User> userList, Order order){
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		for(User user : userList) {
			recipients.add(user.getStr("email"));   //只有email
			phones.add(user.getStr("phone"));    //只有phone
		}
		String[] recipient = recipients.toArray(new String[recipients.size()]);
		String[] phone = phones.toArray(new String[phones.size()]);
		Notice.newOrder(mailSender, smsSender, userinfo, order, recipient, phone);
		recipients.clear();
		phones.clear();
	}
	
	/**
	 * 工单撤回消息提醒。
	 * @param order
	 */
	public void recallOrder(Order order){
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		//运维人员列表
		List<Record> dealList=new ArrayList<Record>();
		List<UserOrder> userOrderList=UserOrder.dao.findBy(" order_id=?", order.getLong("id"));
		for(UserOrder userorder:userOrderList){
			Record accept_user=UserInfo.dao.getAllUserInfo(userorder.get("user_id"));
			dealList.add(accept_user);
		}
		
		for (Record deal : dealList) {
			recipients.add(deal.getStr("uemail"));
			phones.add(deal.getStr("uphone"));
		}
		String[] recipient = recipients.toArray(new String[recipients.size()]);
		String[] phone = phones.toArray(new String[phones.size()]);
		Notice.recallOrder(mailSender, smsSender, userinfo, order, recipient, phone);
		recipients.clear();
		phones.clear();
		
	}
	
	/**
	 * 重新设置邮件，短信提醒内容，每个处理环节，发送短信提醒。
	 * @param userinfoList 		接收邮件和短信的用户列表
	 * @param offer 					工单提交者
	 * @param deal 					工单处理者，即当前用户
	 * @param order 				工单内容
	 * @param comment 			工单处理意见
	 * @param selectProgress 	工单处理情况
	 * @return boolean
	 */
	public boolean updateOrderAndAlert(List<Record> userinfoList,Record offer,Record deal,Order order,Comment comment,int selectProgress){
		if(!userinfoList.isEmpty()){
			for (Record user : userinfoList) {
				recipients.add(user.getStr("useremail"));
				phones.add(user.getStr("userphone"));
			}
			String[] recipient = recipients.toArray(new String[recipients.size()]);
			String[] phone = phones.toArray(new String[phones.size()]);
			Notice.dealOrder(mailSender, smsSender, offer, deal, order, comment, selectProgress, recipient, phone);
			recipients.clear();
			phones.clear();
			return true;
		}
		return false;
	}

}
