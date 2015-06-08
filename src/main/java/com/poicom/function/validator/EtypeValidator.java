package com.poicom.function.validator;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.model.Etype;

public class EtypeValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		if(getActionKey().equals("/admin/type/save")|getActionKey().equals("/admin/type/update")){
			if(ValidateKit.isNullOrEmpty("etype.name")){
				
				addError("nameMsg", "输入内容不为空");
			}else if(!ValidateKit.isLength(c.getPara("etype.name"), 5, 15)){
				addError("nameMsg", "输入字数不少于5...");
			}
		}

	}

	@Override
	protected void handleError(Controller c) {
		if(getActionKey().equals("/admin/type/save")){
			c.keepModel(Etype.class);
			c.render("/app/admin/type/add.html");
		}else if(getActionKey().equals("/admin/type/update")){
			c.keepModel(Etype.class);
			c.render("/app/admin/type/edit.html");
		}

	}

}
