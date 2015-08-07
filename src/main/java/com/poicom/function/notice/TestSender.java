package com.poicom.function.notice;

import org.junit.Test;

import com.poicom.function.notice.factory.*;

public class TestSender {
	
	@Test
	public void test(){
		Provider provider = new SendMailFactory();
		Sender sender = provider.produce();
		sender.send();
	}

}
