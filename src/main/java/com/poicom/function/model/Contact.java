package com.poicom.function.model;

import cn.dreampie.tablebind.TableBind;
/**
 * 
 * @author 唐东宇
 *
 */
@TableBind(tableName="add_contact")
public class Contact extends BaseModel<Contact>{
	
	private static final long serialVersionUID = 1191574096316444005L;
	public static final Contact dao=new Contact();
	

}
