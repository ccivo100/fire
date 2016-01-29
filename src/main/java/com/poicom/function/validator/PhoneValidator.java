package com.poicom.function.validator;

import java.util.List;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.model.Phone;

public class PhoneValidator extends Validator {

    @Override
    protected void validate(Controller c) {
        if(getActionKey().equals("/admin/phone/save")){
            if(ValidateKit.isNullOrEmpty(c.getPara("phone.name").trim())){
                addError("nameMsg", "部门名称不可为空");
            }
            List<Phone> phones=Phone.dao.rootNode(" pid=? ",0);
            
            if(ValidateKit.isNullOrEmpty(c.getPara("phone.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
                if(c.getParaToLong("phone.pid")!=0){
                    addError("pidMsg","父节点应该为0");
                }
            }else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
                boolean flag=false;
                StringBuffer str=new StringBuffer();
                for(Phone phone:phones){
                    str.append(phone.get("id")+" ");
                    if(phone.getLong("id")==c.getParaToLong("pid")){
                        
                        flag=true;
                    }
                }
                if(!flag){
                    addError("pidMsg","父节点应为："+str.toString());
                }
            }
            
        }
        //更新部门验证
        else if(getActionKey().equals("/admin/phone/update")){
            if(ValidateKit.isNullOrEmpty(c.getPara("phone.name").trim())){
                addError("nameMsg", "部门名称不可为空");
            }
            if(ValidateKit.isNullOrEmpty(c.getPara("phone.pid").trim())){
                addError("pidMsg","父节点不可为空");
                
            }else{
                Phone phone=Phone.dao.findById(c.getParaToInt("phone.id"));
                if(phone.getLong("pid")==0&c.getParaToLong("phone.pid")!=phone.getLong("pid")){
                    addError("pidMsg","根节点不可修改");
                }else if(phone.getLong("pid")!=0&c.getParaToLong("phone.pid")==0){
                    addError("pidMsg","子节点不可修改为根节点");
                }
                else if(c.getParaToLong("phone.pid")!=0){
                    List<Phone> phones=Phone.dao.rootNode(" pid=? ",0);
                    boolean flag=false;
                    StringBuffer str=new StringBuffer();
                    for(Phone a:phones){
                        str.append(a.get("id")+" ");
                        if(c.getParaToLong("phone.pid")==a.getLong("id")){
                            flag=true;
                        }
                    }
                    if(!flag){
                        addError("pidMsg","父节点应为："+str.toString());
                    }
                }
            }
        }

    }

    @Override
    protected void handleError(Controller c) {
        if(getActionKey().equals("/admin/phone/save")){
            if(ValidateKit.isNullOrEmpty(c.getPara("phone.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
                c.keepPara("phone.pid");
            }else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
                c.keepPara("pid");
            }
            c.render("/app/admin/phone/add.html");
        }else if(getActionKey().equals("/admin/phone/update")){
            c.keepModel(Phone.class);
            c.render("/app/admin/phone/edit.html");
        }

    }

}
