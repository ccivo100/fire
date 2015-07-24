package com.poicom.function.notice;

import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poicom.basic.kit.StringKit;
import com.poicom.basic.plugin.mail.ExecutorMailer;
import com.poicom.function.model.NoticeMail;

/**
 * 发送邮件
 * @author FireTercel 2015年7月24日 
 *
 */
public class MailSender implements Sender {
	
	public final static Logger log = LoggerFactory.getLogger(MailSender.class);
	
	private String subject;
	private String body;
	private String recipient;
	private String[] recipients;
	
	public String getSubject() {
		return subject;
	}
	public MailSender setSubject(String subject) {
		this.subject = subject;
		return this;
	}
	public String getBody() {
		return body;
	}
	public MailSender setBody(String body) {
		this.body = body;
		return this;
	}
	public String getRecipient() {
		return recipient;
	}
	public MailSender setRecipient(String recipient) {
		this.recipient = recipient;
		return this;
	}
	public String[] getRecipients() {
		return recipients;
	}
	public MailSender setRecipients(String[] recipients) {
		this.recipients = recipients;
		return this;
	}

	/**
	 * 执行发送邮件操作
	 */
	@Override
	public void send() {
		try {
			ExecutorMailer.sendHtml(1,getSubject(), getBody(), getRecipients());
			new NoticeMail()
			.set("subject", getSubject())
			.set("body", getBody())
			.set("recipients", StringKit.arrayToString(getRecipients()))
			.set("created_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).save();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

}
