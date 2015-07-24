package com.poicom.function.model;

import cn.dreampie.tablebind.TableBind;

@TableBind(tableName="com_notice_sms")
public class NoticeSms extends BaseModel<NoticeSms> {
	
	private static final long serialVersionUID = -3801105593019330760L;
	public final static NoticeSms dao =new NoticeSms();

}
