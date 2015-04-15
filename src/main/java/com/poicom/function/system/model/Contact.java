package com.poicom.function.system.model;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;

@TableBind(tableName="add_contact")
public class Contact extends Model<Contact>{
	
	private static final long serialVersionUID = 1191574096316444005L;
	public static final Contact dao=new Contact();
	

}
