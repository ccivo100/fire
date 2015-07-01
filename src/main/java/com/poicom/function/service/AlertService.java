package com.poicom.function.service;

import java.util.List;

import cn.dreampie.ValidateKit;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.AlertKit;
import com.poicom.basic.thread.ThreadAlert;
import com.poicom.function.model.Alertinfo;

/**
 * 邮件、短信提醒业务层。
 * @author FireTercel 2015年6月25日 
 *
 */
public class AlertService extends BaseService {
	
	private static Logger log = Logger.getLogger(AlertService.class);
	
	public static final AlertService service = new AlertService();
	
	public List<Alertinfo> list(){
		List<Alertinfo> alertinfoList=Alertinfo.dao.list(" 1=1 order by created_at asc");
		Alertinfo.dao.format(alertinfoList, 35,"emailcontext","smscontext");
		return alertinfoList;
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

}
