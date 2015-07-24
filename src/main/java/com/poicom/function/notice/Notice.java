package com.poicom.function.notice;

import com.jfinal.plugin.activerecord.Record;
import com.poicom.function.model.Comment;
import com.poicom.function.model.Order;
import com.poicom.function.notice.template.MailTemplate;
import com.poicom.function.notice.template.SmsTemplate;

public class Notice {
	
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
