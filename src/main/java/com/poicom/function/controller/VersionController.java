package com.poicom.function.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.poicom.function.model.Phone;
import com.poicom.function.model.Version;
import com.poicom.function.service.PhoneService;
import com.poicom.function.service.VersionService;
import com.poicom.function.validator.VersionValidator;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;

@ControllerKey(value = "/admin/version", path="/app/admin")
public class VersionController extends BaseController {
    
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(VersionController.class);
    
    private final static String VERSION_ADD_PAGE="version/add.html";
    private final static String VERSION_EDIT_PAGE="version/edit.html";
    
    public void index(){
        Page<Version> versionPage;
        String orderby=" ORDER BY version.id ";
        if(ValidateKit.isNullOrEmpty(getPara("version"))){
            String where = " 1=1 ";
            versionPage=Version.dao.findVersionPage(getParaToInt(0,1), 10, where, orderby);
        }else{
            String where=" version.name like ?  ";
            String condition ="%"+getPara("version").trim()+"%";
            versionPage=Version.dao.findVersionPage(getParaToInt(0,1), 10, where, orderby, condition);
            setAttr("version",getPara("version").trim());
        }
        setAttr("versionPage",versionPage);
        render("version.html");
    }
    
    /**
     * 准备新增职位
     */
    public void add(){
        List<Phone> rootPhone=PhoneService.service.rootNode();
        setAttr("rootPhone",rootPhone);
        render(VERSION_ADD_PAGE);
    }
    
    /**
     * 新增职位
     */
    @Before(VersionValidator.class)
    public void save(){
        Version version=getModel(Version.class);
        
        VersionService.service.save(version);
        redirect("/admin/version");
    }
    
    /**
     * 准备更新职位
     */
    public void edit(){
        Version version=Version.dao.findById(getPara("id"));
        List<Phone> rootPhone=PhoneService.service.rootNode();
        setAttr("rootPhone",rootPhone);
        setAttr("version",version);
        render(VERSION_EDIT_PAGE);
    }
    
    /**
     * 更新职位
     */
    @Before(VersionValidator.class)
    public void update(){
        Version version=getModel(Version.class);
        VersionService.service.update(version);
        redirect("/admin/version");
    }
    
    /**
     * 设置不可用
     */
    public void off(){
        VersionService.service.off(Version.dao.findById(getPara("id")));
        redirect("/admin/version");
    }
    
    /**
     * 设置可用
     */
    public void on(){
        VersionService.service.on(Version.dao.findById(getPara("id")));
        redirect("/admin/version");
    }

}
