package com.poicom.function.app.model;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;

@TableBind(tableName = "com_apartment")
public class Apartment extends Model<Apartment> {

	private static final long serialVersionUID = 5493122811350290559L;
	public static Apartment dao=new Apartment();

}
