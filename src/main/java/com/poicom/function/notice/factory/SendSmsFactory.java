package com.poicom.function.notice.factory;

import com.poicom.function.notice.Provider;
import com.poicom.function.notice.Sender;
import com.poicom.function.notice.SmsSender;

public class SendSmsFactory implements Provider {

	@Override
	public Sender produce() {
		return new SmsSender();
	}

}
