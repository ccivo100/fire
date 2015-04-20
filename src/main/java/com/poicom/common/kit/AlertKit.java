package com.poicom.common.kit;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.mail.EmailException;

import cn.dreampie.ValidateKit;
import cn.dreampie.encription.EncriptionKit;
import cn.dreampie.mail.Mailer;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;
import com.poicom.function.app.model.Order;
import com.poicom.function.system.model.Retrieve;
import com.poicom.function.system.model.User;

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
	 * @描述 报障员提交新故障工单时调用
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
	 * @描述 报障员催办时使用
	 * @param offer
	 * @param deal
	 * @param order
	 * @return
	 */
	public static StringBuffer getMailBody(Record offer,Record deal,Order order){
		StringBuffer body=new StringBuffer();
		body.append("尊敬的 "+deal.getStr("fullname")+" 您好，")
		.append("你有一条来自 "+offer.getStr("bname")+" 的 ")
		.append(offer.getStr("ufullname")+"（"+offer.getStr("uphone")+")")
		.append("于 "+DateKit.format(order.getDate("created_at"),DateKit.pattern_ymd_hms)+" ")
		.append("发起的故障申告，现已超时，请尽快处理！");
		return body;
	}
	
	/**
	 * @描述 用户重置密码时使用。
	 * @param user
	 * @param context
	 * @return
	 */
	public static StringBuffer getMailBody(User user,String context){
		
		String newValidCode=EncriptionKit.encrypt(user.getStr("email")+new Date());
		new Retrieve()
		.set("email", user.getStr("email"))
		.set("random", newValidCode).save();
		
		String retrieve="http://"+context+"/repassword?email="+user.get("email")+"&newValidCode="+newValidCode;
		
		StringBuffer body=new StringBuffer();
		body.append("亲爱的用户，"+user.getStr("full_name")+"，您好！<br/>")
		.append("您在 "+DateKit.format(new Date(),DateKit.pattern_ymd_hms)+" 提交了密码重置的请求。")
		.append("请您在24小时内点击下面的链接重置您的密码：<br/>")
		.append("<br/>")
		.append("请点击该链接<a href=\""+retrieve+"\"> 重置密码！</a>（该链接24小时有效）<br/>")
		.append("<br/>")
		.append("若您无法点击链接，也可复制以下地址到浏览器地址栏中：<br/>")
		.append("<a href='"+retrieve+"'>"+retrieve+"</a>");
		
		return body;
	}
	
	
	public static StringBuffer getMailBody(User offer,User deal,Order order){
		StringBuffer body=new StringBuffer();
		body.append("尊敬的"+offer.getStr("full_name")+"，您好！<br/>")
		.append("您在 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms))
		.append(" 提交的故障工单，已由 "+deal.getStr("full_name")+"（"+deal.getStr("phone")+"）")
		//.append("于 "+DateKit.format(order.getDate("deal_at"),DateKit.pattern_ymd_hms))
		.append(" 处理完毕，感谢您使用故障申报系统，祝您生活愉快！");
		
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
	
	/**
	 * @描述 报障员提交新故障工单时调用
	 * @param userinfo
	 * @param phone
	 */
	public static void sendSms(Record userinfo,String... phone ){
		
		StringBuffer contt=new StringBuffer();
		if(ValidateKit.isNullOrEmpty(userinfo)){
			contt.append("null");
		}else{
			contt.append("您好，")
			.append(userinfo.getStr("bname")+"的 ")
			.append(userinfo.getStr("ufullname"))
			.append(" ("+userinfo.getStr("uphone")+") ")
			.append("提交了故障工单，请尽快处理。【一点通】");
		}
		
		String content =contt.toString();
		sendSms(content,phone);
		
	}
	
	/**
	 * @描述 报障员催办时使用
	 * @param userinfo
	 * @param phone
	 */
	public static void sendSms(Record offer,Order order,String... phone ){
		StringBuffer contt=new StringBuffer();
		if(ValidateKit.isNullOrEmpty(offer)){
			contt.append("null");
		}else{
			contt.append("您好，")
			.append("你有一条来自 "+offer.getStr("bname")+" 的 ")
			.append(offer.getStr("ufullname")+"（"+offer.getStr("uphone")+"）")
			.append(" 于 "+DateKit.format(order.getDate("created_at"),DateKit.pattern_ymd_hms)+" ")
			.append("发起的故障申告，现已超时，请尽快处理！【一点通】");
		}
		
		String content =contt.toString();
		sendSms(content,phone);
	}
	
	/**
	 * @描述 工单处理完反馈。
	 * @param offer
	 * @param deal
	 * @param order
	 */
	public static void sendSms(User offer,User deal,Order order){
		
		StringBuffer contt=new StringBuffer();
		if(ValidateKit.isNullOrEmpty(offer)){
			contt.append("null");
		}else{
			contt.append("尊敬的"+offer.getStr("full_name")+"，您好！")
			.append("您在 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms))
			.append(" 提交的故障工单，已由 "+deal.getStr("full_name")+"（"+deal.getStr("phone")+"）")
			//.append("于 "+DateKit.format(order.getDate("deal_at"),DateKit.pattern_ymd_hms))
			.append(" 处理完毕，感谢您使用故障申报系统，祝您生活愉快！【一点通】");
		}
		
		String content =contt.toString();
		sendSms(content,offer.getStr("phone"));
	}
	
	/**
	 * @描述 提供定时提醒短信
	 * @param phone
	 */
	public static void sendSms(Object paras,String... phone ){
		
		StringBuffer contt=new StringBuffer();

		contt.append("您好，您所属故障类型工单已超时未处理，请及时处理。【一点通】");
		
		String content =contt.toString();
		sendSms(content,phone);
		
	}
	
	/**
	 * @描述 短信接口
	 * @param content
	 * @param phone
	 */
	public static void sendSms(String content,String... phone){
		
		//电话列表...
		String phones=phoneFormat(phone);
		String department="xx";
		String name="xx";
		String type="xx";
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
