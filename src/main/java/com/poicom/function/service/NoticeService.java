package com.poicom.function.service;

import java.util.List;

import cn.dreampie.ValidateKit;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.poicom.basic.kit.AlertKit;
import com.poicom.basic.kit.WebKit;
import com.poicom.basic.thread.ThreadAlert;
import com.poicom.function.model.Alertinfo;
import com.poicom.function.model.NoticeMail;
import com.poicom.function.model.NoticeSms;

/**
 * 邮件、短信提醒业务层。
 * @author FireTercel 2015年6月25日 
 *
 */
public class NoticeService extends BaseService {
	
	private static Logger log = Logger.getLogger(NoticeService.class);
	
	public static final NoticeService service = new NoticeService();
	
	public List<Alertinfo> list(){
		List<Alertinfo> alertinfoList=Alertinfo.dao.list(" 1=1 order by created_at desc");
		Alertinfo.dao.format(alertinfoList, 35,"emailcontext","smscontext");
		return alertinfoList;
	}
	
	public Page<Alertinfo> page(int pageNumber,int pageSize,Object... paras){
		Page<Alertinfo> alertinfoPage = Alertinfo.dao.page(pageNumber, pageSize, " 1=1 order by created_at desc ", paras);
		return alertinfoPage;
	}
	
	public Page<NoticeMail> pageToMail(int pageNumber,int pageSize,Object... paras){
		Page<NoticeMail> mailPage = NoticeMail.dao.page(pageNumber, pageSize, " 1=1 order by created_at desc ", paras);
		format(mailPage);
		return mailPage;
	}
	
	public Page<NoticeSms> pageToSms(int pageNumber,int pageSize,Object... paras){
		Page<NoticeSms> smsPage = NoticeSms.dao.page(pageNumber, pageSize, " 1=1 order by created_at desc ", paras);
		return smsPage;
	}
	
	/**
	 * 邮件、短信内容加入线程
	 * @param EmailTitle
	 * @param EmailBody
	 * @param emailAddr
	 * @param smsContext
	 * @param phone
	 */
	public void doAlert(String EmailTitle,String EmailBody,String emailAddr,String smsContext,String phone){
		AlertKit alertKit = new AlertKit();
		if (ValidateKit.isEmail(emailAddr)){
			alertKit.setEmailTitle(EmailTitle)
						.setEmailBody(EmailBody)
						.setEmailAdd(emailAddr);
		}
		// 电话非空
		if (!ValidateKit.isNullOrEmpty(phone)) {
			if (ValidateKit.isPhone(phone)) {
				alertKit.setSmsContext(smsContext)
							.setSmsPhone(phone);
			}
		}
		// 加入进程
		log.info("日志添加到入库队列 ---> 处理故障工单");
		ThreadAlert.add(alertKit);
	}
	
	public void format(Page<NoticeMail> mailPage){
		for(NoticeMail mail : mailPage.getList()){
			mail.set("body", WebKit.getHTMLToString(mail.getStr("body")));
		}
	}
	

}
