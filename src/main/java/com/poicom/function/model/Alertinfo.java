package com.poicom.function.model;

import cn.dreampie.tablebind.TableBind;

@TableBind(tableName = "com_alertinfo")
public class Alertinfo extends BaseModel<Alertinfo> {

	private static final long serialVersionUID = 8981730605238407324L;
	
	public static final Alertinfo dao = new Alertinfo();

}
