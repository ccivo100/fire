package com.poicom.basic.plugin.mail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorMailer {
	
	private static Logger log = LoggerFactory.getLogger(ExecutorMailer.class);
	private static ExecutorService executorService = null;
	
	private final static int POOL_SIZE = 50;
	
	public static ExecutorService getExecutorService(){
		return getExecutorService(1,null);
	}
	public static ExecutorService getExecutorService(int poolNum){
		return getExecutorService(poolNum,null);
	}
	
	/**
	 * 1：缓存型线程池；
	 * 2：固定线程池；
	 * 3：调度型线程池；
	 * 4：单例线程；
	 * @param poolNum
	 * @param threadFactory
	 * @return
	 */
	public static ExecutorService getExecutorService(int poolNum, ThreadFactory threadFactory){
		if(executorService == null){
			switch(poolNum){
			case 1:
				executorService = threadFactory == null ? Executors.newCachedThreadPool():Executors.newCachedThreadPool(threadFactory);
				break;
			case 2:
				executorService = threadFactory == null ? Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE) : Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE,threadFactory);
				break;
			case 3:
				//同参数为1的FixedThreadPool
				executorService = threadFactory == null ? Executors.newSingleThreadExecutor() : Executors.newSingleThreadExecutor(threadFactory);
				break;
			case 4:
				//线程池里面最多有paras个线程
				executorService = threadFactory == null ? Executors.newScheduledThreadPool(100) : Executors.newScheduledThreadPool(100,threadFactory);
				break;
			default:
				executorService = threadFactory == null ? Executors.newCachedThreadPool():Executors.newCachedThreadPool(threadFactory);
				break;
			}
		}
		return executorService;
	}
	
	public static void setExecutorService(ExecutorService executorService){
		ExecutorMailer.executorService = executorService;
	}
	
	/**
	 * @param subject		主题
	 * @param body			内容
	 * @param recipients	收件人
	 */
	public static void sendText(int poolNum,final String subject, final String body, final String... recipients){
		getExecutorService(poolNum).execute(getSendTextRunnable(subject, body, recipients));
	}
	
	private static Runnable getSendTextRunnable(final String subject, final String body, final String... recipients) {
		return new Runnable() {
			public synchronized void alert() {
				try {
					Mailer.sendText(subject, body, recipients);
				} catch (EmailException e) {
					e.printStackTrace();
				}
			}
			public void run() {
				alert();
			}
		};
	}
	
  /**
   * @param subject    主题
   * @param body       内容
   * @param recipients 收件人
   */
	public static void sendHtml(int poolNum,final String subject, final String body, final String... recipients) throws EmailException {
		sendHtml(poolNum,subject, body, null, recipients);
	}

  /**
   * @param subject    主题
   * @param body       内容
   * @param attachment 附件
   * @param recipients 收件人
   */
	public static void sendHtml(int poolNum,final String subject, final String body, final EmailAttachment attachment,
			final String... recipients) {
		getExecutorService(poolNum).execute(getSendHtmlRunnable(subject, body, attachment, recipients));
	}
	
	private static Runnable getSendHtmlRunnable(final String subject, final String body, final EmailAttachment attachment,
			final String... recipients) {
		return new Runnable() {
			public synchronized void alert() {
				try {
					Mailer.sendHtml(subject, body, recipients);
				} catch (EmailException e) {
					e.printStackTrace();
				}
			}
			public void run() {
				alert();
			}
		};
	}
	
	/**
	 * @param subject    主题
	 * @param body       内容
	 * @param attachment 附件
	 * @param recipients 收件人
	 */
	public static void sendAttachment(int poolNum,final String subject, final String body, final EmailAttachment attachment,
			final String... recipients) {
		getExecutorService(poolNum).execute(getSendAttachRunnable(subject, body, attachment, recipients));
	}
	
	private static Runnable getSendAttachRunnable(final String subject, final String body, final EmailAttachment attachment,
			final String... recipients) {
		return new Runnable() {
			public synchronized void alert() {
				try {
					Mailer.sendAttachment(subject, body, attachment, recipients);
				} catch (EmailException e) {
					e.printStackTrace();
				}
			}
			public void run() {
				alert();
			}
		};
	}

	@Test
	public void test(){
		MailerPlugin mailerPlugin = new MailerPlugin();
		boolean result =mailerPlugin.start();
		if(result){
			String[] recipients ={"fireterceltong@poicom.net","283784513@qq.com"};
			try {
				sendHtml(1,"测试", "<h1>测试内容</h1><br><a href=\"http://www.baidu.com\">baidu</a>", recipients);
			} catch (EmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}