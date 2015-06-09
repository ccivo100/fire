package com.poicom.function.controller;

import java.util.List;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.poicom.function.model.Etype;
import com.poicom.function.service.EtypeService;
import com.poicom.function.validator.EtypeValidator;

@ControllerKey(value = "/admin/type", path="/app/admin")
public class EtypeController extends BaseController {
	
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(EtypeController.class);
	
	private final static String TYPE_ADD_PAGE="type/add.html";
	private final static String TYPE_EDIT_PAGE="type/edit.html";
	
	public void index(){
		List<Etype> etypes = EtypeService.service.rootNode();
		EtypeService.service.childNode(etypes);
		setAttr("etypeTree",etypes);
		render("type.html");
	}
	
	/**
	 * 准备新增故障类型
	 */
	public void add(){
		if(ValidateKit.isNullOrEmpty(getPara("id"))){

		}else{
			System.out.println(getParaToLong("id"));
			setAttr("pid",getParaToLong("id"));
		}
		render(TYPE_ADD_PAGE);
	}
	
	/**
	 * 新增故障类型
	 */
	@Before(EtypeValidator.class)
	public void save(){
		Etype etype=getModel(Etype.class);
		EtypeService.service.save(etype);
		redirect("/admin/type");
	}
	
	/**
	 * 准备更新故障类型
	 */
	public void edit(){
		Etype etype=Etype.dao.findById(getPara("id"));
		setAttr("etype",etype);
		render(TYPE_EDIT_PAGE);
	}
	
	/**
	 * 更新故障类型
	 */
	@Before(EtypeValidator.class)
	public void update(){
		Etype etype=getModel(Etype.class);
		EtypeService.service.update(etype);
		redirect("/admin/type");
	}
	
	public void off(){
		EtypeService.service.off(Etype.dao.findById(getPara("id")));
		redirect("/admin/type");
	}
	
	public void on(){
		EtypeService.service.on(Etype.dao.findById(getPara("id")));
		redirect("/admin/type");
	}

}
