package com.poicom.function.model;

import cn.dreampie.tablebind.TableBind;
/**
 * 部门-类型 Model
 * @author 陈宇佳
 *
 */
@TableBind(tableName = "com_phone_type")
public class PhoneType extends BaseModel<PhoneType> {

	private static final long serialVersionUID = 3292410641820412495L;
	public final static PhoneType dao=new PhoneType();

}
