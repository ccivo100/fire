package com.poicom.function.app.model;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;

@TableBind(tableName = "com_apartment_type")
public class ApartmentType extends Model<ApartmentType> {
	
	private static final long serialVersionUID = -8360827092353805689L;
	public final static ApartmentType dao=new ApartmentType();

}
