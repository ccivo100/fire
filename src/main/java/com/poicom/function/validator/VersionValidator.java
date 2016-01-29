package com.poicom.function.validator;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.model.Version;
import com.poicom.function.service.PhoneService;

public class VersionValidator extends Validator {

    @Override
    protected void validate(Controller c) {
        if(getActionKey().equals("/admin/version/save")|getActionKey().equals("/admin/version/update")){
            if(ValidateKit.isNullOrEmpty(c.getPara("version.name"))){
                
                addError("nameMsg", "输入内容不为空");
            }else if(!ValidateKit.isLength(c.getPara("version.name"), 3, 15)){
                addError("nameMsg", "输入字数不少于3...");
            }
            if(c.getParaToInt("version.phone_id")==-1){
                addError("phoneMsg", "请选择所属部门");
            }
        }

    }

    @Override
    protected void handleError(Controller c) {
        if(getActionKey().equals("/admin/version/save")){
            c.keepModel(Version.class);
            c.setAttr("rootPhone", PhoneService.service.rootNode());
            c.render("/app/admin/version/add.html");
        }else if(getActionKey().equals("/admin/version/update")){
            c.keepModel(Version.class);
            c.setAttr("rootPhone", PhoneService.service.rootNode());
            c.render("/app/admin/version/edit.html");
        }

    }

}
