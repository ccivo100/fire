package com.poicom.function.validator;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.model.Position;
import com.poicom.function.service.ApartmentService;

public class PositionValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		if(getActionKey().equals("/admin/position/save")|getActionKey().equals("/admin/position/update")){
			if(ValidateKit.isNullOrEmpty(c.getPara("position.name"))){
				
				addError("nameMsg", "输入内容不为空");
			}else if(!ValidateKit.isLength(c.getPara("position.name"), 3, 15)){
				addError("nameMsg", "输入字数不少于3...");
			}
			if(c.getParaToInt("position.apartment_id")==-1){
				addError("apartmentMsg", "请选择所属部门");
			}
		}

	}

	@Override
	protected void handleError(Controller c) {
		if(getActionKey().equals("/admin/position/save")){
			c.keepModel(Position.class);
			c.setAttr("rootApartment", ApartmentService.service.rootNode());
			c.render("/app/admin/position/add.html");
		}else if(getActionKey().equals("/admin/position/update")){
			c.keepModel(Position.class);
			c.setAttr("rootApartment", ApartmentService.service.rootNode());
			c.render("/app/admin/position/edit.html");
		}

	}

}
