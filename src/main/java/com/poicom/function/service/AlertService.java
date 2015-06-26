package com.poicom.function.service;

import cn.dreampie.ValidateKit;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.AlertKit;
import com.poicom.basic.thread.ThreadAlert;

/**
 * 邮件、短信提醒业务层。
 * @author FireTercel 2015年6月25日 
 *
 */
public class AlertService extends BaseService {
	
	private static Logger log = Logger.getLogger(AlertService.class);
	
	public static final AlertService service = new AlertService();
	
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

}
