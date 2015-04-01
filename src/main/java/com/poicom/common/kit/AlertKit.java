package com.poicom.common.kit;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.mail.EmailException;

import cn.dreampie.ValidateKit;
import cn.dreampie.mail.Mailer;

import com.jfinal.plugin.activerecord.Record;

/**
 * @描述 发送邮件、短信工具类
 * @author tangdongyu
 *
 */
public class AlertKit {
	
	
	/**
	 * @描述 发送邮件通知
	 *            主题        subject
	 *            内容        body
	 *            接收邮件 paras
	 */
	public static void sendEmail(String subject,String body,String... paras){
		
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
	public static StringBuffer getMailBody(Record offer,String description,String deal){
		
		StringBuffer body=new StringBuffer();
		
		body.append("您好，"+deal+"：<br/>")
		.append("&nbsp;&nbsp;&nbsp;&nbsp;" + offer.getStr("bname") + "的 "
						+ offer.getStr("ufullname") + " ("
						+ offer.getStr("uphone") + ") 发来故障工单。<br/>")
		.append("故障内容："+description+"<br/>")
		.append("请及时处理。【一点通】");
		
		return body;
		
	}
	
	/**
	 * @描述 电话数组格式化
	 */
	private static String phoneFormat(String... phone){
		StringBuffer p=new StringBuffer();
		for(int i=0;i<phone.length;i++){
			if(i==(phone.length-1)){
				p.append(phone[i]);
			}else
				p.append(phone[i]).append(",");
		}
		//电话列表...
		return p.toString();
		
	}
	
	public static void sendSms(Record userinfo,String... phone ){
		
		//电话列表...
		String phones=phoneFormat(phone);
		System.out.println(phones);
		
		String department="XX";
		String name="XX";
		String type="XX";
		
		
		StringBuffer contt=new StringBuffer();
		if(ValidateKit.isNullOrEmpty(userinfo)){
			contt.append("null");
		}else{
			contt.append("您好，")
			.append(userinfo.getStr("bname")+"的 ")
			.append(userinfo.getStr("ufullname"))
			.append(" ("+userinfo.getStr("uphone")+") ")
			.append("提交了故障工单，请尽快处理。");
		}
		
		String content =contt.toString();

		try {
			
			URL url=new URL("http://sms.poicom.net/postsms.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");// 提交模式
			
			conn.setDoOutput(true);// 是否输入参数
			
			StringBuffer params = new StringBuffer();
	        // 表单参数与get形式一样
	        params.append("phones").append("=").append(phones).append("&")
	              .append("department").append("=").append(department).append("&")
	              .append("name").append("=").append(name).append("&")
	              .append("type").append("=").append(type).append("&")
	              .append("content").append("=").append(content);
	        byte[] bypes = params.toString().getBytes();
	        OutputStream outStream= conn.getOutputStream();
	        outStream.write(bypes);// 输入参数
	        outStream.flush();
	        outStream.close();
	        
	        System.out.println(conn.getResponseCode()); //响应代码 200表示成功

			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
