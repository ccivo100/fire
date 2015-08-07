package com.poicom.basic.plugin.mail;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mailer {
	
	private static Logger log = LoggerFactory.getLogger(Mailer.class);
	
	/**
	 * 
	 * @param subject		主题
	 * @param body			内容
	 * @param recipients	收件人
	 * @return result			底层MimeMessage的消息id
	 * @throws EmailException
	 */
	public static String sendText(String subject, String body, String... recipients) throws EmailException{
		SimpleEmail simpleEmail = new SimpleEmail();
		configEmail(subject, simpleEmail, recipients);
		simpleEmail.setMsg(body);
		String result = simpleEmail.send();
		log.info("send email to {}", StringUtils.join(recipients));
		return result;
	}
	
	/**
	 * 
	 * @param subject		主题
	 * @param body			内容
	 * @param recipients	收件人
	 * @return result			底层MimeMessage的消息id
	 * @throws EmailException
	 */
	public static String sendHtml(String subject, String body, String... recipients) throws EmailException{
		return sendHtml(subject, body, null, recipients);
	}
	
	/**
	 * 
	 * @param subject			主题
	 * @param body				内容
	 * @param attachment	附件
	 * @param recipients		收件人
	 * @return result				底层MimeMessage的消息id
	 * @throws EmailException
	 */
	public static String sendHtml(String subject, String body, EmailAttachment attachment, String... recipients) throws EmailException{
		HtmlEmail htmlEmail = new HtmlEmail();
		configEmail(subject, htmlEmail, recipients);
		htmlEmail.setHtmlMsg(body);
		htmlEmail.setTextMsg("Your email client does not support HTML messages.");
		if(attachment != null){
			htmlEmail.attach(attachment);
		}
		String result = htmlEmail.send();
		log.info("send email to {}", StringUtils.join(recipients));
		return result;
	}
	
	/**
	 * 
	 * @param subject			主题
	 * @param body				内容
	 * @param attachment	附件
	 * @param recipients		收件人
	 * @return result				底层MimeMessage的消息id
	 * @throws EmailException
	 */
	public static String sendAttachment(String subject, String body, EmailAttachment attachment, String... recipients) throws EmailException{
		MultiPartEmail multiPartEmail = new MultiPartEmail();
		configEmail(subject, multiPartEmail, recipients);
		multiPartEmail.setMsg(body);
		if(attachment != null){
			multiPartEmail.attach(attachment);
		}
		String result = multiPartEmail.send();
		log.info("send email to {}", StringUtils.join(recipients));
		return result;
	}
	
	public static void configEmail(String subject, Email email, String... recipients) throws EmailException{
		MailerConf mailerConf = MailerPlugin.mailerConf;
		email.setCharset(mailerConf.getCharset());
		email.setSocketTimeout(mailerConf.getTimeout());
		email.setSocketConnectionTimeout(mailerConf.getConnectout());
		email.setCharset(mailerConf.getEncode());
	    email.setHostName(mailerConf.getHost());
	    if(!mailerConf.getSslport().isEmpty()){
	    	email.setSslSmtpPort(mailerConf.getSslport());
	    }
	    if(!mailerConf.getPort().isEmpty()){
	    	email.setSmtpPort(Integer.parseInt(mailerConf.getPort()));
	    }
	    email.setSSLOnConnect(mailerConf.isSsl());
	    email.setStartTLSEnabled(mailerConf.isTls());
	    email.setDebug(mailerConf.isDebug());
	    email.setAuthentication(mailerConf.getUser(), mailerConf.getPassword());
	    email.setFrom(mailerConf.getFrom(), mailerConf.getName());
	    email.setSubject(subject);
	    email.addTo(recipients);
	}

}
