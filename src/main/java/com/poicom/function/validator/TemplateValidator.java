package com.poicom.function.validator;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class TemplateValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		if(getActionKey().equals("/admin/template/save")|getActionKey().equals("/admin/template/update")){
			String[] selectReceiver;
			selectReceiver = c.getParaValues("selectReceiver[]");    //  选择邮件接收者 multiple or not
			if(c.getParaToLong("selectType")==-1){
				addError("state","请选择故障大类！");
			}else if(c.getParaToLong("template.typeid")==-1){
				addError("state","请选择故障小类！");
			}else if(c.getParaToLong("selectApartment")==-1){
				addError("state","请选择运维部门！");
			}else if(c.getParaToLong("selectChildApartment")==-1){
				addError("state","请选择运维组别！");
			}
			else if(ValidateKit.isNullOrEmpty(selectReceiver)){
				addError("state","请选择邮件接收人员！");
			}else if (ValidateKit.isNullOrEmpty(c.getPara("template.context"))){
				addError("state","模板内容不能为空！");
			}
		}
	}

	@Override
	protected void handleError(Controller c) {
		if(getActionKey().equals("/admin/template/save")|getActionKey().equals("/admin/template/update")){
			c.renderJson("state", c.getAttr("state"));
		}
	}

}
