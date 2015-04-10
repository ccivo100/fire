package com.poicom.function.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class IndexValidator extends Validator{
	
	protected Logger logger=LoggerFactory.getLogger(getClass());

	@Override
	protected void validate(Controller c) {
		
		if(getActionKey().equals("/")){
			boolean isLength=ValidateKit.isLength(c.getPara("condition"), 0, 10);
			if(!isLength){
				addError("searchMsg","查询字数不可超过10，请重新输入");
			}
		}
		
	}

	@Override
	protected void handleError(Controller c) {
		
		c.keepPara("condition");
		c.render("index.html");
	}
	
	

}
