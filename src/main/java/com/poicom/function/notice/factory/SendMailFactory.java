package com.poicom.function.notice.factory;

import com.poicom.function.notice.MailSender;
import com.poicom.function.notice.Provider;
import com.poicom.function.notice.Sender;

public class SendMailFactory implements Provider {

	@Override
	public Sender produce() {
		return new MailSender();
	}

}
