package com.poicom.function.validator;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.model.Branch;

public class BranchValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		if(getActionKey().equals("/admin/branch/save")|getActionKey().equals("/admin/branch/update")){
			if(ValidateKit.isNullOrEmpty("branch.name")){
				
				addError("nameMsg", "输入内容不为空");
			}else if(!ValidateKit.isLength(c.getPara("branch.name"), 4, 15)){
				addError("nameMsg", "输入字数不少于4...");
			}
		}

	}

	@Override
	protected void handleError(Controller c) {
		
		if(getActionKey().equals("/admin/branch/save")){
			c.keepModel(Branch.class);
			c.render("/app/admin/branch/add.html");
		}else if(getActionKey().equals("/admin/branch/update")){
			c.keepModel(Branch.class);
			c.render("/app/admin/branch/edit.html");
		}

	}

}
