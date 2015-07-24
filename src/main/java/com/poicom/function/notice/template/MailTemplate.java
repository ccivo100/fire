package com.poicom.function.notice.template;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import cn.dreampie.encription.EncriptionKit;

import com.jfinal.plugin.activerecord.Record;
import com.poicom.basic.kit.DateKit;
import com.poicom.function.model.Comment;
import com.poicom.function.model.Order;
import com.poicom.function.model.Retrieve;
import com.poicom.function.model.User;

/**
 * 邮件模板
 * @author FireTercel 2015年7月24日 
 *
 */
public class MailTemplate {
	
	/**
	 * 新建故障单发送邮件
	 * @param offer
	 * @param order
	 * @return
	 */
	public static String newOrderMailBody(Record offer,Order order){
		StringBuffer body = new StringBuffer();
		body.append("尊敬的用户，您好：<br/>")
				.append(offer.getStr("bname") + " 的 "+ offer.getStr("ufullname"))
				.append(" （"+ offer.getStr("uphone") + "）")
				.append("发来故障工单。<br/>")
				.append("故障内容为："+order.getStr("description")+"<br/>")
				.append("请及时处理。");
		return body.toString();
	}
	
	/**
	 * 新建故障单发送邮件-TO-同部门人员
	 * @param offer
	 * @param order
	 * @return
	 */
	public static String newOrderToOwnMailBody(Record offer,Order order){
		StringBuffer body=new StringBuffer();
		body.append("尊敬的用户，您好：<br/>")
				.append("贵部门的"+offer.getStr("ufullname"))
				.append(" （"+ offer.getStr("uphone") + "）")
				.append("提交了故障工单。<br/>")
				.append("标题为："+order.getStr("title")+"<br/>")
				.append("<p>故障内容为："+order.getStr("description")+"</p><br/>")
				.append("详情请登陆系统查看。");
		return body.toString();
	}
	
	/**
	 * 撤回故障单发送邮件
	 * @param offer
	 * @param order
	 * @return
	 */
	public static String recallOrderMailBody(Record offer,Order order){
		StringBuffer body=new StringBuffer();
		body.append("尊敬的用户，您好，<br>")
				.append(offer.getStr("bname")+" 的 ")
				.append(offer.getStr("ufullname")+"（"+offer.getStr("uphone")+"）")
				.append("撤回于 "+DateKit.format(order.getDate("offer_at"),DateKit.pattern_ymd_hms)+" ")
				.append("发起的故障申告！");
		return body.toString();
	}
	
	/**
	 * 重置密码发送邮件
	 * @param user
	 * @param context
	 * @return
	 */
	public static String resetPasswordMailBody(User user,String context){
		String newValidCode=EncriptionKit.encrypt(user.getStr("email")+new Date());
		new Retrieve()
		.set("username", user.getStr("username"))
		.set("email", user.getStr("email"))
		.set("random", newValidCode)
		.save();
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
		
		return body.toString();
	}
	
	/**
	 * 每个处理环节，发送邮件
	 * @param offer
	 * @param deal
	 * @param order
	 * @param comment
	 * @param selectProgress
	 * @return
	 */
	public static String dealOrderMailBody(Record offer,Record deal,Order order,Comment comment,int selectProgress){
		StringBuffer body=new StringBuffer();
		body.append("尊敬的用户，你好，<br/>")
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
		return body.toString();
	}

}
