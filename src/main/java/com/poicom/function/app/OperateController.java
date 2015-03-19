package com.poicom.function.app;

import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;

import cn.dreampie.ValidateKit;
import cn.dreampie.mail.Mailer;
import cn.dreampie.routebind.ControllerKey;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.poicom.function.app.model.ErrorType;
import com.poicom.function.app.model.Order;
import com.poicom.function.user.model.User;
import com.poicom.function.user.model.UserInfo;

/**
 * @描述 运维管理
 * @author poicom7
 *
 */
@ControllerKey(value="/operate",path="/page/app/operate")
public class OperateController extends Controller{
	
	public void index(){
		
	}
	
	/**
	 * @描述 运维人员查询本账号处理范围内的故障工单
	 */
	public void deal(){
		User user=User.dao.getCurrentUser();
		
		Page <Record> operatePage=Order.dao.getOperateOrderPage(getParaToInt(0,1), 10, user.get("id"));
		Order.dao.format(operatePage,"description");
		setAttr("operatePage",operatePage);
		render("operate.html");
	}
	
	/**
	 * @描述 进入故障工单处理页面
	 */
	public void edit(){
		//故障类型
		setAttr("typeList",ErrorType.dao.getAllType());
		
		//工单详细信息
		Record order = Order.dao.getCommonOrder(getParaToInt("id"));
		setAttr("order", order);
		
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		setAttr("userinfo",userinfo);
		
		//获取工单申报者的分公司信息
		Record offer=UserInfo.dao.getUserBranch(order.getLong("offerid"));

		if(!ValidateKit.isNullOrEmpty(offer))
			setAttr("offer_branch",offer.getStr("branch"));

		
	}
	
	/**
	 * @描述 提交故障处理建议
	 */
	@Before(Tx.class)
	public void update(){
		
		//order_id
		Integer orderid=getParaToInt("orderid");
		//description
		String comment=getPara("commen");
		
		Order.dao.findById(orderid).set("deal_user", getParaToInt("dealid"))
				.set("comment", comment)
				.set("deal_at", 
						DateTime.now().toString("yyyy-MM-dd HH:mm:ss"))
				.set("status", 1).update();
		
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		
		redirect("/operate/deal");
	}
	
	/**
	 * @描述 发送邮件通知
	 *            主题        subject
	 *            内容        body
	 *            接收邮件 paras
	 */
	public void sendEmail(String subject,String body,String... paras){
		
		try {
			Mailer.sendHtml(subject, body, paras);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 邮件内容 body
	 * @param offer
	 * @param description
	 * @param deal
	 * @return
	 */
	public StringBuffer getMailBody(Record offer,String description,String deal){
		
		StringBuffer body=new StringBuffer();
		
		body.append("您好，"+deal+"：<br/>")
		.append("&nbsp;&nbsp;&nbsp;&nbsp;" + offer.getStr("branch") + "的 "
						+ offer.getStr("fullname") + " ("
						+ offer.getStr("phone") + ") 发来故障工单。<br/>")
		.append("故障内容："+description+"<br/>")
		.append("请及时处理。");
		
		return body;
		
	}

}
