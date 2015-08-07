package com.poicom.function.notice.builder;

import java.util.ArrayList;
import java.util.List;

import com.poicom.function.notice.MailSender;
import com.poicom.function.notice.Sender;
import com.poicom.function.notice.SmsSender;

/**
 * 建造者模式，即包含一个集合，用于保存批量通知对象。
 * @author FireTercel 2015年7月24日 
 *
 */
public class Builder {
	
	private List<Sender> list = new ArrayList<Sender>();
	
	public void produceMailSender(int count) {
		for (int i = 0; i < count; i++) {
			list.add(new MailSender());
		}
	}

	public void produceSmsSender(int count) {
		for (int i = 0; i < count; i++) {
			list.add(new SmsSender());
		}
	}

}
