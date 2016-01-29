package com.poicom.function.validator;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.model.Product;

public class ProductValidator extends Validator {
    

    @Override
    protected void validate(Controller c) {
        if(getActionKey().equals("/admin/product/save")|getActionKey().equals("/admin/product/update")){
            if(ValidateKit.isNullOrEmpty("branch.name")){
                
                addError("nameMsg", "输入内容不为空");
            }else if(!ValidateKit.isLength(c.getPara("product.name"), 1, 15)){
                addError("nameMsg", "输入字数不少于1...");
            }
        }

    }

    @Override
    protected void handleError(Controller c) {
        
        if(getActionKey().equals("/admin/product/save")){
            c.keepModel(Product.class);
            c.render("/app/admin/product/add.html");
        }else if(getActionKey().equals("/admin/product/update")){
            c.keepModel(Product.class);
            c.render("/app/admin/product/edit.html");
        }

    }

}
