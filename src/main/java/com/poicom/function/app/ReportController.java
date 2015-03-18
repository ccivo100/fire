package com.poicom.function.app;


import java.util.Date;
import java.util.List;

import org.apache.commons.mail.EmailException;

import cn.dreampie.ValidateKit;
import cn.dreampie.mail.Mailer;
import cn.dreampie.routebind.ControllerKey;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.poicom.function.app.model.Order;
import com.poicom.function.user.model.User;
import com.poicom.function.user.model.UserInfo;

/**
 * @描述 故障申报
 * @author poicom7
 *
 */
@ControllerKey(value = "/report", path = "/page/app/report")
public class ReportController extends Controller{
	
	public void index(){
		
		render("order");
	}

	
	
	/**
	 * @描述 报障人员查询本账号申报的故障工单
	 */
	public void offer(){
		User user=User.dao.getCurrentUser();
		System.out.println(user.get("id"));
		setAttr("reportPage",Order.dao.getReportOrderPage(getParaToInt(0,1), 10,user.get("id")));
		render("report.html");
	}
	
	
	//测试
	public void print(){
		System.out.println("id="+getPara("id"));
		redirect("/report/offer");
	}
	
	/**
	 * @描述 进入新建故障工单页面
	 */
	public void add(){
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		setAttr("userinfo",userinfo);
		setAttr("typeList",Order.dao.getAllType());
		render("add.html");
	}
	
	/**
	 * @描述 新建故障工单 并发送邮件、短信通知
	 */
	@Before(ReportValidator.class)
	public void save(){
		
		//获取表单数据，填充进Order
		Order order=new Order().set("offer_user", getParaToInt("userid"))
				.set("description", getPara("description"))
				.set("type", getParaToInt("selectType"))
				.set("status", 0).set("offer_at", new Date());
		if(order.save())
			redirect("/report/offer");
		else
			System.out.println("userid:" + getParaToInt("userid") + " type:"
					+ getParaToInt("selectType") + " des:"
					+ getPara("description"));
		
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		//运维人员列表
		List<Record> dealList=User.dao.getOperatorList(getParaToInt("selectType"));
		
		//发送邮件
		for (Record deal : dealList) {
			//获取邮件内容
			String body=getMailBody(userinfo,getPara("description"),deal.getStr("fullname")).toString();
			//发送邮件通知
			if(ValidateKit.isEmail(deal.getStr("useremail"))){
				sendEmail("点通故障系统提醒您！",body,deal.getStr("useremail"));
			}
			
		}
		
		redirect("/report/offer");
	}
	
	/**
	 * @描述 申报人员可以修改未被处理的工单
	 *            此时不会再次发送邮件、短信通知。
	 */
	public void edit(){
		
		//故障类型
		setAttr("typeList",Order.dao.getAllType());
		
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
	 *  @描述 处理申报人员更新故障工单操作 
	 */
	@Before(Tx.class)
	public void update(){
		//order_id
		Integer orderid=getParaToInt("orderid");
		//description
		String description=getPara("description");
		
		Order.dao.findById(orderid).set("description", getPara("description")).set("updated_at", new Date()).update();
		
		redirect("/report/offer");
		
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
	 * @描述 发送短信通知
	 */
	public void sendSms(){
		
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
