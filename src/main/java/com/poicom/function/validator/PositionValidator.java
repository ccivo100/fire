package com.poicom.function.validator;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.model.Position;

public class PositionValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		if(getActionKey().equals("/admin/position/save")|getActionKey().equals("/admin/position/update")){
			if(ValidateKit.isNullOrEmpty("position.name")){
				
				addError("nameMsg", "输入内容不为空");
			}else if(!ValidateKit.isLength(c.getPara("position.name"), 3, 15)){
				addError("nameMsg", "输入字数不少于3...");
			}
		}

	}

	@Override
	protected void handleError(Controller c) {
		if(getActionKey().equals("/admin/position/save")){
			c.keepModel(Position.class);
			c.render("/app/admin/position/add.html");
		}else if(getActionKey().equals("/admin/position/update")){
			c.keepModel(Position.class);
			c.render("/app/admin/position/edit.html");
		}

	}

}
