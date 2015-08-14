package com.poicom.function.validator;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class TemplateValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		if(getActionKey().equals("/admin/template/save")|getActionKey().equals("/admin/template/update")){
			String[] selectApartment,selectReceiver;
			
			if(c.getParaValues("selectApartment")==null){
				selectApartment=c.getParaValues("selectApartment[]");
			}else{
				selectApartment=c.getParaValues("selectApartment");
			}
			
			selectReceiver = c.getParaValues("selectReceiver[]");    //  选择邮件接收者 multiple or not
			if (ValidateKit.isNullOrEmpty(c.getPara("template.title"))){
				addError("state","模板标题不能为空！");
			}else if(c.getParaToLong("selectType")==-1){
				addError("state","请选择故障大类！");
			}else if(c.getParaToLong("selectChildType")==-1){
				addError("state","请选择故障小类！");
			}else if(ValidateKit.isNullOrEmpty(selectApartment[0])){
				addError("state","请选择运维部门！");
			}else if(selectApartment.length==1 && c.getParaToLong("selectChildApartment")==-1){
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
