package com.poicom.common.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.poicom.function.app.model.Order;
import com.poicom.function.user.model.User;
import com.poicom.function.user.model.UserInfo;

public class JFController extends Controller{
	
	/**
	 * @描述 Record 转换为Attr
	 * @param record
	 */
	public void setAttr(Record record){
		String[] column=record.getColumnNames();
		for(int i=0;i<column.length;i++){
			//System.out.println(column[i]+"："+record.get(column[i]));
			setAttr(column[i],record.get(column[i]));
		}
	}
	
	

}
