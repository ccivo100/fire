package com.poicom.function.notice;

import com.jfinal.plugin.activerecord.Record;
import com.poicom.function.model.Comment;
import com.poicom.function.model.Order;
import com.poicom.function.model.User;
import com.poicom.function.notice.template.MailTemplate;
import com.poicom.function.notice.template.SmsTemplate;

public class Notice {
	
	public static void retrieve(MailSender mailSender, SmsSender smsSender, User user, String context, String[] recipient, String[] phone){
		mailSender
			.setSubject("【新提醒】找回密码提醒！")
			.setBody(MailTemplate.resetPasswordMailBody(user, context))
			.setRecipients(recipient);
		smsSender
			.setMessage("找回密码，重置密码的链接（24小时内有效）已经发到您邮箱，让及时登录处理")
			.setPhones(phone);
		mailSender.send();
		smsSender.send();
			
	}
	
	public static void newOrderToOwn(MailSender mailSender, SmsSender smsSender, Record offer,Order order, String[] recipient, String[] phone){
		mailSender
			.setSubject("【新提醒】新故障工单提醒！")
			.setBody(MailTemplate.newOrderToOwnMailBody(offer, order))
			.setRecipients(recipient);
		smsSender
			.setMessage(SmsTemplate.newOrderToOwnSmsMsg(offer, order))
			.setPhones(phone);
		mailSender.send();
		smsSender.send();
	}
	
	public static void newOrder(MailSender mailSender, SmsSender smsSender, Record offer,Order order, String[] recipient, String[] phone){
		mailSender
			.setSubject("【新提醒】新故障工单提醒！")
			.setBody(MailTemplate.newOrderMailBody(offer, order))
			.setRecipients(recipient);
		smsSender
			.setMessage(SmsTemplate.newOrderSmsMsg(offer, order))
			.setPhones(phone);
		mailSender.send();
		smsSender.send();
	}
	
	/**
	 * 新工单-邮件提醒运维人员
	 * @param mailSender
	 * @param offer
	 * @param order
	 * @param recipient
	 */
	public static void newOrder(MailSender mailSender, Record offer,Order order, String[] recipient){
		mailSender
			.setSubject("【新提醒】新故障工单提醒！")
			.setBody(MailTemplate.newOrderMailBody(offer, order))
			.setRecipients(recipient);
		mailSender.send();
	}
	
	/**
	 * 新工单-短信提醒运维人员
	 * @param smsSender
	 * @param offer
	 * @param order
	 * @param phone
	 */
	public static void newOrder(SmsSender smsSender, Record offer,Order order, String[] phone){
		smsSender
			.setMessage(SmsTemplate.newOrderSmsMsg(offer, order))
			.setPhones(phone);
		smsSender.send();
	}
	
	public static void recallOrder(MailSender mailSender, SmsSender smsSender, Record offer,Order order, String[] recipient, String[] phone){
		mailSender
			.setSubject("【撤回通知】故障申报撤回提醒！")
			.setBody(MailTemplate.recallOrderMailBody(offer, order))
			.setRecipients(recipient);
		smsSender
			.setMessage(SmsTemplate.recallOrderSmsMsg(offer, order))
			.setPhones(phone);
		mailSender.send();
		smsSender.send();
	}
	
	public static void dealOrder(MailSender mailSender, SmsSender smsSender, Record offer, Record deal, Order order, Comment comment, int selectProgress, String[] recipient, String[] phone){
		mailSender
			.setSubject("【处理通知】故障申报处理情况提醒！")
			.setBody(MailTemplate.dealOrderMailBody(offer, deal, order, comment, selectProgress))
			.setRecipients(recipient);
		smsSender
			.setMessage(SmsTemplate.dealOrderSmsMsg(offer, deal, order, selectProgress))
			.setPhones(phone);
		mailSender.send();
		smsSender.send();
	}
	
	

}
