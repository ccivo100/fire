package com.poicom.function.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poicom.function.model.User;
import com.poicom.function.notice.MailSender;
import com.poicom.function.notice.Notice;
import com.poicom.function.notice.SmsSender;
import com.poicom.function.notice.factory.SendMailFactory;
import com.poicom.function.notice.factory.SendSmsFactory;

public class IndexService extends BaseService {
	
	private static Logger log = LoggerFactory.getLogger(IndexService.class);
	
	public static IndexService serivce = new IndexService();
	
	private MailSender mailSender;
	private SmsSender smsSender;
	
	public IndexService(){
		mailSender = (MailSender) new SendMailFactory().produce();
		smsSender = (SmsSender) new SendSmsFactory().produce();
	}
	
	private List<String> recipients = new ArrayList<String>();
	private List<String> phones = new ArrayList<String>();
	
	public void retrieve(String username, String email, String context){
		User user=User.dao.findFirstBy("`user`.username = ? AND `user`.deleted_at is null", username);
		recipients.add(user.getStr("email"));
		phones.add(user.getStr("phone"));
		String[] recipient = recipients.toArray(new String[recipients.size()]);
		String[] phone = phones.toArray(new String[phones.size()]);
		Notice.retrieve(mailSender, smsSender, user, context, recipient, phone);
		recipients.clear();
		phones.clear();
	}

}
