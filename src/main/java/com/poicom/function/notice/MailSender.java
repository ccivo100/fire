package com.poicom.function.notice;

import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poicom.basic.kit.StringKit;
import com.poicom.basic.plugin.mail.ExecutorMailer;
import com.poicom.basic.plugin.mail.MailerPlugin;
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
	
	@Test
	public void test(){
		MailerPlugin mailer = new MailerPlugin();
		mailer.start();
		MailSender mail = new MailSender();
		mail.setBody("尊敬的用户，你好，<br/>话务部的章佳微（13767012721）于2015-08-05 19:11:12，提交关于“易百年手机不能拨打电话”的故障单。现由软件研发部的陈宇佳处理完毕。<br/>故障内容为：13925317410易百年手机用户不能拨打电话，开始显示正在注册，核对三码都正常，也同步了数据，移动网正常，版本为2015-3-3，地图定位正常，拔卡与不拔卡均恢复了出厂设置，后多次测试拨打电话，显示无法连接，也无二代提交记录，麻烦相关领导处理，谢谢。<br/>处理意见为：请指导客户到经销商处刷至最新版本，同时请客户将手机卡插到智能机上测试是否能使用流量上网浏览网页，若无法正常上网浏览网页，则让客户致电手机卡所属营运商反映情况，协助客户恢复上网。待智能机能正常上网后，指导客户把卡放回刷至最新版本的365手机，带卡进行恢复出厂设置。在确保信号栏信号标示正常、桌面出现网络标示G或E的情况下，再查看“我的信息”中有没有三码显示，如正常显示，则可尝试拨打电话。。<br/>详情请登陆系统查看。");
		String[] str = new String[]{"yoyocheung@ydto.cn", "kentsiu@poicom.net", "joeywong@poicom.net", "bobolee@poicom.net", "ansonchu@poicom.net", "yvonnezhao@poicom.net", "ceciyeung@poicom.net", "dianacheng@poicom.net", "lingaduo@poicom.net", "yoyochia@poicom.net", "lilyfung@poicom.net", "winlee@poicom.net", "vernawong@poicom.net", "garylam@poicom.net", "coralong@poicom.net", "rebeccaho@poicom.net", "Anniesiu@ydto.cn", "lindalau@ydto.cn", "lynnelau@ydto.cn", "jenchan@ydto.cn", "connietao@ydto.cn", "LucyFang@ydto.cn", "Alisacheung@ydto.cn", "LilyChow@ydto.cn", "CandyWei@ydto.cn", "QueenieChan@ydto.cn", "HuanhuanNgai@ydto.cn", "kimiliu@ydto.cn", "roseko@ydto.cn", "ailisacheung@ydto.cn", "tomxiong@ydto.cn", "vivianzhang@ydto.cn", "snowhsu@ydto.cn", "jacksonchan@ydto.cn", "aliceyu@ydto.cn", "abbyhu@ydto.cn", "yanpang@ydto.cn", "candylau@ydto.cn", "lilichan@ydto.cn", "cocotse@ydto.cn", "mikichan@ydto.cn", "annxiong@ydto.cn", "evelynhu@ydto.cn", "katherinetong@ydto.cn", "kaleryeung@ydto.cn", "elainechan1@poicom.net"};
		mail.setRecipients(str);
		mail.setSubject("【处理通知】故障申报处理情况提醒！");
		mail.send();
	}

}
