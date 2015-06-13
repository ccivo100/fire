package com.poicom.function.model;

import cn.dreampie.tablebind.TableBind;
/**
 * 部门-类型 Model
 * @author 唐东宇
 *
 */
@TableBind(tableName = "com_apartment_type")
public class ApartmentType extends BaseModel<ApartmentType> {
	
	private static final long serialVersionUID = -8360827092353805689L;
	public final static ApartmentType dao=new ApartmentType();

}
