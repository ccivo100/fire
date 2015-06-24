package com.poicom.basic.kit;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;

import cn.dreampie.ValidateKit;
import cn.dreampie.encription.EncriptionKit;
import cn.dreampie.mail.Mailer;

import com.jfinal.plugin.activerecord.Record;
import com.poicom.function.model.Comment;
import com.poicom.function.model.Order;
import com.poicom.function.model.Retrieve;
import com.poicom.function.model.User;

/**
 * @描述 发送邮件、短信工具类
 * @author tangdongyu
 *
 */
public class AlertKit {
	
	//邮件标题
	private String EmailTitle;
	//邮件内容
	private String EmailBody;
	//收件人邮箱
	private String emailAdd;
	
	//短信内容
	private String smsContext;
	//接收人手机号码
	private String[] smsPhone;
	
	
	
	public String getEmailTitle() {
		return EmailTitle;
	}

	public AlertKit setEmailTitle(String emailTitle) {
		EmailTitle = emailTitle;
		return this;
	}

	public String getEmailBody() {
		return EmailBody;
	}

	public AlertKit setEmailBody(String emailBody) {
		EmailBody = emailBody;
		return this;
	}

	public String getEmailAdd() {
		return emailAdd;
	}

	public AlertKit setEmailAdd(String emailAdd) {
		this.emailAdd = emailAdd;
		return this;
	}

	public String getSmsContext() {
		return smsContext;
	}

	public AlertKit setSmsContext(String smsContext) {
		this.smsContext = smsContext;
		return this;
	}

	public String[] getSmsPhone() {
		return smsPhone;
	}

	public AlertKit setSmsPhone(String... smsPhone) {
		this.smsPhone = smsPhone;
		return this;
	}
	
	//发送邮件
	public void sendEmail(){
		AlertKit alertKit=new AlertKit();
		sendEmail(alertKit.getEmailTitle(),alertKit.getEmailBody(),alertKit.getEmailAdd());
	}
	
	//发送短信
	public void sendSms(){
		AlertKit alertKit=new AlertKit();
		sendSms(alertKit.getSmsContext(),alertKit.getSmsPhone());
	}

	/**
	 * @描述 发送邮件通知
	 *            主题        subject
	 *            内容        body
	 *            接收邮件 paras
	 */
	public static void sendEmail(String subject,String body,String... paras){
		
		try {
			System.out.println("进入发送邮件通知主方法！");
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
		.append("请及时处理。");
		return body;
	}
	
	/**
	 * 提交故障工单发送邮件
	 * @param offer
	 * @param deal
	 * @param order
	 * @param description
	 * @return
	 */
	public static StringBuffer getMailBody(Record offer,Record deal,Order order,String description){
		StringBuffer body=new StringBuffer();
		body.append("尊敬的 "+deal.getStr("fullname")+"，您好：<br/>")
		.append(offer.getStr("bname") + " 的 "+ offer.getStr("ufullname"))
		.append(" （"+ offer.getStr("uphone") + "）")
		//.append(" 于 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms)+" ")
		.append("发来故障工单。<br/>")
		.append("故障内容为："+description+"<br/>")
		.append("请及时处理。");
		
		return body;
	}
	
	/**
	 * @描述 报障员提交新故障工单时调用
	 * @param userinfo
	 * @param phone
	 */
	public static String setSmsContext(Object obj,Record userinfo,Order order ){
		
		StringBuffer contt=new StringBuffer();
		if(ValidateKit.isNullOrEmpty(userinfo)){
			contt.append("null");
		}else{
			contt.append("您好，")
			.append(userinfo.getStr("bname")+"的 ")
			.append(userinfo.getStr("ufullname"))
			.append("（"+userinfo.getStr("uphone")+"） ")
			//.append("于 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms))
			.append("提交了关于”"+StringUtils.abbreviate(order.getStr("title"),10)+"“的故障工单，请尽快处理。");
		}
		return contt.toString();
	}
	
	/**
	 * 工单指派二级部门发送邮件
	 * C：change；O：order
	 * @param offer
	 * @param deal
	 * @param order
	 * @param description
	 * @return
	 */
	public static StringBuffer getCOMailBody(Record offer,Record deal,Order order,String description){
		StringBuffer body=new StringBuffer();
		body.append("尊敬的 "+deal.getStr("fullname")+"，您好：<br/>")
		.append(offer.getStr("bname") + " 的 "+ offer.getStr("ufullname"))
		.append(" （"+ offer.getStr("uphone") + "）")
		//.append(" 于 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms)+" ")
		.append("发来故障工单。<br/>")
		.append("故障内容为："+description+"<br/>")
		.append("请及时处理。（指派）");
		return body;
	}
	
	/**
	 * 工单指派二级部门发送短信
	 * @param obj
	 * @param userinfo
	 * @param order
	 * @return
	 */
	public static String setCOSmsContext(Record userinfo,Order order ){
		StringBuffer contt=new StringBuffer();
		if(ValidateKit.isNullOrEmpty(userinfo)){
			contt.append("null");
		}else{
			contt.append("您好，")
			.append(userinfo.getStr("bname")+"的 ")
			.append(userinfo.getStr("ufullname"))
			.append("（"+userinfo.getStr("uphone")+"） ")
			//.append("于 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms))
			.append("提交了故障工单，请尽快处理。（指派）");
		}
		return contt.toString();
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
		.append(offer.getStr("ufullname")+"（"+offer.getStr("uphone")+"）")
		.append("于 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms)+" ")
		.append("发起的故障申告，现已超时，请尽快处理！");
		return body;
	}
	
	/**
	 * @描述 报障员催办时使用
	 * @param userinfo
	 * @param phone
	 */
	@Deprecated
	public static String getSmsBody(Record offer,Order order){
		StringBuffer contt=new StringBuffer();
		if(ValidateKit.isNullOrEmpty(offer)){
			contt.append("null");
		}else{
			contt.append("您好，")
			.append("你有一条来自 "+offer.getStr("bname")+" 的 ")
			.append(offer.getStr("ufullname")+"（"+offer.getStr("uphone")+"）")
			.append(" 于 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms)+" ")
			.append("发起的故障申告，现已超时，请尽快处理！");
		}
		
		return contt.toString();
	}
	
	/**
	 * @描述 报障员撤回工单时使用
	 * @param offer
	 * @param deal
	 * @param order
	 * @return
	 */
	public static StringBuffer getMailBodyRecall(Record offer,Record deal,Order order){
		StringBuffer body=new StringBuffer();
		body.append("【撤回通知】尊敬的 "+deal.getStr("ufullname")+" 您好，")
		.append(offer.getStr("bname")+" 的 ")
		.append(offer.getStr("ufullname")+"（"+offer.getStr("uphone")+"）")
		.append("撤回于 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms)+" ")
		.append("发起的故障申告！");
		return body;
	}
	
	/**
	 * @描述 申报员撤回故障工单通知
	 * @param offer
	 * @param order
	 * @return
	 */
	public static String getSmsBodyRecall(Record offer,Order order){
		StringBuffer contt=new StringBuffer();
		if(ValidateKit.isNullOrEmpty(offer)){
			contt.append("null");
		}else{
			contt.append("您好，")
			.append(offer.getStr("bname")+" 的 ")
			.append(offer.getStr("ufullname")+"（"+offer.getStr("uphone")+"）")
			.append("撤回于 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms)+" ")
			.append("发起的故障申告！");
		}
		return contt.toString();
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
		.set("username", user.getStr("username"))
		.set("email", user.getStr("email"))
		.set("random", newValidCode).save();
		
		String retrieve="http://"+context+"/repassword?username="+user.getStr("username")+"&email="+user.get("email")+"&newValidCode="+newValidCode;
		
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
	
	public static StringBuffer getMailBodyArrange(Record offeruser,Record acceptuser,Record dealuser,Order order){
		StringBuffer body=new StringBuffer();
		body.append("亲爱的用户，"+offeruser.getStr("ufullname")+"，您好！<br/>")
		.append("您在"+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms))
		.append("提交的关于“"+order.get("title")+"”的故障工单已由")
		.append(acceptuser.getStr("ufullname")+"（"+acceptuser.getStr("uphone")+"）")
		.append("指派给"+dealuser.getStr("ufullname")+"（"+dealuser.getStr("uphone")+"）")
		.append("处理。<br/>")
		.append("感谢您使用点通科技故障申报系统，祝您生活愉快！");
		
		return body;
	}
	
	/**
	 * @描述 运维人员处理完毕。
	 * @param offer
	 * @param deal
	 * @param order
	 * @return
	 */
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
	 * 运维处理完毕通知申报人部门所有人员。
	 * @param user
	 * @param offer
	 * @param deal
	 * @param order
	 * @return
	 */
	public static StringBuffer getMailBody(User user,User offer,User deal,Order order){
		StringBuffer body=new StringBuffer();
		body
		.append("尊敬的"+user.getStr("full_name")+"，您好！<br/>")
		.append(offer.getStr("full_name"))
		.append("在 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms))
		.append(" 提交的故障工单，已由 "+deal.getStr("full_name")+"（"+deal.getStr("phone")+"）")
		.append(" 处理完毕，感谢您使用故障申报系统，祝您生活愉快！");
		
		return body;
	}
	
	/**
	 * @描述 工单处理完反馈。
	 * @param offer
	 * @param deal
	 * @param order
	 */
	public static String getSmsBody(User offer,User deal,Order order){
		StringBuffer contt=new StringBuffer();
		if(ValidateKit.isNullOrEmpty(offer)){
			contt.append("null");
		}else{
			contt.append("尊敬的"+offer.getStr("full_name")+"，您好！")
			.append("您在 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms))
			.append(" 提交的故障工单，已由 "+deal.getStr("full_name")+"（"+deal.getStr("phone")+"）")
			//.append("于 "+DateKit.format(order.getDate("deal_at"),DateKit.pattern_ymd_hms))
			.append(" 处理完毕！");
		}
		return contt.toString();
	}
	
	/**
	 *  每个处理环节，发送邮件提醒。
	 * @param user
	 * @param offer
	 * @param deal
	 * @param order
	 * @param selectProgress
	 * @return
	 */
	public static StringBuffer getDealMailBody(Record user,Record offer,Record deal,Order order,Comment comment,int selectProgress){
		StringBuffer body=new StringBuffer();
		body.append("尊敬的"+user.getStr("fullname")+"，你好。<br/>")
				.append(offer.getStr("aname")+"的"+offer.getStr("ufullname")+"（"+offer.getStr("uphone")+"）")
				.append("于"+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms)+"，")
				.append("提交关于“"+StringUtils.abbreviate(order.getStr("title"),15)+"”"+"的故障单。")
				.append("现由"+deal.getStr("aname")+"的"+deal.getStr("ufullname"));
		if(selectProgress == 0){
			body.append("开始处理。<br/>");
		}else if(selectProgress == 1){
			body.append("继续处理。<br/>");
		}else if(selectProgress == 2){
			body.append("处理完毕。<br/>");
		}else if(selectProgress == 3){
			body.append("转派处理。<br/>");
		}
		body.append("处理意见为："+comment.getStr("context")+"。<br/>");
		body.append("详情请登陆系统查看。");
		return body;
	}
	
	/**
	 * 每个处理环节，发送短信提醒。
	 * @param user
	 * @param offer
	 * @param deal
	 * @param order
	 * @return
	 */
	public static StringBuffer getDealSmsBody(Record user,Record offer,Record deal,Order order,int selectProgress){
		StringBuffer body=new StringBuffer();
		body.append("尊敬的"+user.getStr("fullname")+"，")
				.append(offer.getStr("aname")+"的"+offer.getStr("ufullname"))
				.append("于"+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms)+"，")
				.append("提交关于“"+StringUtils.abbreviate(order.getStr("title"),15)+"”"+"的故障单。")
				.append("现由"+deal.getStr("aname")+"的"+deal.getStr("ufullname"));
		if(selectProgress == 0){
			body.append("开始处理");
		}else if(selectProgress == 1){
			body.append("继续处理");
		}else if(selectProgress == 2){
			body.append("处理完毕");
		}else if(selectProgress == 3){
			body.append("转派处理");
		}
		
		body.append("，详情请登陆系统查看。");
		return body;
	}
	
	
	
	/**
	 * 运维处理完毕通知申报人部门所有人员。
	 * @param user
	 * @param offer
	 * @param deal
	 * @param order
	 * @return
	 */
	public static String getSmsBody(User user,User offer,User deal,Order order){
		StringBuffer contt=new StringBuffer();
		if(ValidateKit.isNullOrEmpty(user)){
			contt.append("null");
		}else{
			contt
			.append("尊敬的"+user.getStr("full_name")+"，您好！")
			.append(offer.getStr("full_name"))
			.append("在 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms))
			.append(" 提交的故障工单，已由 "+deal.getStr("full_name")+"（"+deal.getStr("phone")+"）")
			//.append("于 "+DateKit.format(order.getDate("deal_at"),DateKit.pattern_ymd_hms))
			.append(" 处理完毕！");
		}
		return contt.toString();
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
	 * @描述 运维人员指派通知
	 * @param offeruser
	 * @param acceptuser
	 * @param dealuser
	 * @param order
	 * @return
	 */
	@Deprecated
	public static String getSmsBodyArrange(Record offeruser,Record acceptuser,Record dealuser,Order order){
		StringBuffer contt=new StringBuffer();
		contt.append("亲爱的 "+offeruser.getStr("ufullname")+"，您好！")
		.append("您在"+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms))
		.append("提交的，关于“"+order.get("title")+"”的故障工单，已由")
		.append(acceptuser.getStr("ufullname")+"（"+acceptuser.getStr("uphone")+"）")
		.append("指派给"+dealuser.getStr("ufullname")+"（"+dealuser.getStr("uphone")+"）")
		.append("处理。");
		return contt.toString();
	}
	
	
	/**
	 * @描述 提供定时提醒短信
	 * @param phone
	 */
	@Deprecated
	public static String getSmsBodyOuttime( ){
		
		StringBuffer contt=new StringBuffer();
		contt.append("您好，您所属故障类型工单已超时未处理，请及时处理。");
		//String content =contt.toString();
		//sendSms(content,phone);
		return contt.toString();
	}
	
	/**
	 * @描述 短信接口
	 * @param content
	 * @param phone
	 */
	public static int sendSms(String content,String... phone){
		
		System.out.println("进入发送短信主方法");
		//电话列表...
		String phones=phoneFormat(phone);
		String department="研发部";
		String name="firetercel";
		String type="1";
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
	        return conn.getResponseCode();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
