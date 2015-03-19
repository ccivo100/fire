package com.poicom.function.app;


import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.validate.Validator;
import com.poicom.function.app.model.ErrorType;

public class ReportValidator extends Validator{

	@Override
	protected void validate(Controller c) {
		
		if(getActionKey().equals("/report/save")){
			
			if(c.getParaToInt("selectType")==-1){
				addError("descriptionMsg","请选择故障类型");
			}
			
		}
		
	}

	@Override
	protected void handleError(Controller c) {
		
		
		c.keepModel(ErrorType.class);
		c.keepPara("order");
		c.keepPara("userinfo");
		c.render("/page/app/report/add.html");
	}

}