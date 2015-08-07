package com.poicom.function.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import com.poicom.function.model.Alertinfo;
import com.poicom.function.model.NoticeMail;
import com.poicom.function.model.NoticeSms;
import com.poicom.function.service.NoticeService;

import cn.dreampie.routebind.ControllerKey;

@ControllerKey(value = "/admin/notice", path = "/app/admin")
public class NoticeController extends BaseController {
	
	private static Logger log = LoggerFactory.getLogger(NoticeController.class);
	
	private final static String ALERTINFO_QUERY_PAGE="alertinfo/query.html";
	private final static String ALERTINFO_MAIL_PAGE="notice/mail.html";
	private final static String ALERTINFO_SMS_PAGE="notice/sms.html";
	
	public void index(){
		Page<Alertinfo> alertinfoPage;
		alertinfoPage=NoticeService.service.page(getParaToInt(0,1), 10);
		setAttr("alertinfoPage",alertinfoPage);
		render("alertinfo.html");
	}
	
	public void mail(){
		Page<NoticeMail> mailPage;
		mailPage = NoticeService.service.pageToMail(getParaToInt(0,1), 10);
		setAttr("mailPage",mailPage);
		render(ALERTINFO_MAIL_PAGE);
	}
	
	public void sms(){
		Page<NoticeSms> smsPage;
		smsPage = NoticeService.service.pageToSms(getParaToInt(0,1), 10);
		setAttr("smsPage",smsPage);
		render(ALERTINFO_SMS_PAGE);
	}
	
	public void query(){
		Long alertinfoid = getParaToLong("alertinfoid");
		Alertinfo alertinfo = Alertinfo.dao.findById(alertinfoid);
		renderJson("alertinfo",alertinfo);
	}
	public void queryMail(){
		Long maiid = getParaToLong("mailid");
		NoticeMail mail = NoticeMail.dao.findById(maiid);
		renderJson("mail", mail);
	}
	public void querySms(){
		Long smsid = getParaToLong("smsid");
		NoticeSms sms = NoticeSms.dao.findById(smsid);
		renderJson("sms",sms);
	}
	
	public void delete(){
		Long alertinfoid = getParaToLong("alertinfoid");
		Alertinfo alertinfo = Alertinfo.dao.findById(alertinfoid);
		if(alertinfo.delete()){
			renderJson("state","删除成功！");
		}else
			renderJson("state","删除失败！");
	}
	public void deleteMail(){
		Long maiid = getParaToLong("mailid");
		NoticeMail mail = NoticeMail.dao.findById(maiid);
		if(mail.delete()){
			renderJson("state","删除成功！");
		}else
			renderJson("state","删除失败！");
	}
	public void deleteSms(){
		Long smsid = getParaToLong("smsid");
		NoticeSms sms = NoticeSms.dao.findById(smsid);
		if(sms.delete()){
			renderJson("state","删除成功！");
		}else
			renderJson("state","删除失败！");
	}

}
