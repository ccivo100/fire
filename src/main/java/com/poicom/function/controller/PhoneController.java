package com.poicom.function.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.poicom.function.model.Phone;
import com.poicom.function.service.PhoneService;
import com.poicom.function.validator.PhoneValidator;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;

/**
 * 部门Controller
 * @author FireTercel 2015年6月8日 
 *
 */
@ControllerKey(value = "/admin/phone", path="/app/admin")
public class PhoneController extends BaseController {
    
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(PhoneController.class);
    
    private final static String PHONE_ADD_PAGE="phone/add.html";
    private final static String PHONE_EDIT_PAGE="phone/edit.html";
    
    public void index(){
        List<Phone> phones=PhoneService.service.rootNode();
        PhoneService.service.childNode(phones);
        setAttr("phoneTree",phones);
        render("phone.html");
    }
    
    /**
     * 准备新增部门
     */
    public void add(){
        List<Phone> phones=PhoneService.service.rootNode();
        
        if(ValidateKit.isNullOrEmpty(getPara("id"))){

        }else{
            System.out.println(getParaToLong("id"));
            setAttr("pid",getParaToLong("id"));
        }
        setAttr("phones",phones);
        render(PHONE_ADD_PAGE);
    }
    
    /**
     * 新增部门
     */
    @Before(PhoneValidator.class)
    public void save(){
        Phone phone=getModel(Phone.class);
        PhoneService.service.save(phone);
        redirect("/admin/phone");
    }
    
    /**
     * 准备更新部门
     */
    public void edit(){
        Phone phone=Phone.dao.findById(getPara("id"));
        setAttr("phone",phone);
        render(PHONE_EDIT_PAGE);
    }
    
    /**
     * 更新部门
     */
    @Before(PhoneValidator.class)
    public void update(){
        Phone phone=getModel(Phone.class);
        PhoneService.service.update(phone);
        redirect("/admin/phone");
    }
    
    /**
     * 设置不可用
     */
    public void off(){
        PhoneService.service.off(Phone.dao.findById(getPara("id")));
        redirect("/admin/phone");
    }
    
    /**
     * 设置可用
     */
    public void on(){
        PhoneService.service.on(Phone.dao.findById(getPara("id")));
        redirect("/admin/phone");
    }
    
    /**
     * 设置是否可运维
     */
    public void able(){
        Phone phone = Phone.dao.findById(getPara("phoneid"));
        boolean able = PhoneService.service.operate(phone);
        renderJson("state",able);
    }
    
    
}