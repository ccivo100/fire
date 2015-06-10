package com.poicom.function.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.poicom.function.model.Apartment;
import com.poicom.function.service.ApartmentService;
import com.poicom.function.validator.ApartmentValidator;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;

/**
 * 部门Controller
 * @author FireTercel 2015年6月8日 
 *
 */
@ControllerKey(value = "/admin/apartment", path="/app/admin")
public class ApartmentController extends BaseController {
	
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(ApartmentController.class);
	
	private final static String APARTMENT_ADD_PAGE="apartment/add.html";
	private final static String APARTMENT_EDIT_PAGE="apartment/edit.html";
	
	public void index(){
		List<Apartment> apartments=ApartmentService.service.rootNode();
		ApartmentService.service.childNode(apartments);
		setAttr("apartmentTree",apartments);
		render("apartment.html");
	}
	
	/**
	 * 准备新增部门
	 */
	public void add(){
		List<Apartment> apartments=ApartmentService.service.rootNode();
		
		if(ValidateKit.isNullOrEmpty(getPara("id"))){

		}else{
			System.out.println(getParaToLong("id"));
			setAttr("pid",getParaToLong("id"));
		}
		setAttr("apartments",apartments);
		render(APARTMENT_ADD_PAGE);
	}
	
	/**
	 * 新增部门
	 */
	@Before(ApartmentValidator.class)
	public void save(){
		Apartment apartment=getModel(Apartment.class);
		ApartmentService.service.save(apartment);
		redirect("/admin/apartment");
	}
	
	/**
	 * 准备更新部门
	 */
	public void edit(){
		Apartment apartment=Apartment.dao.findById(getPara("id"));
		setAttr("apartment",apartment);
		render(APARTMENT_EDIT_PAGE);
	}
	
	/**
	 * 更新部门
	 */
	@Before(ApartmentValidator.class)
	public void update(){
		Apartment apartment=getModel(Apartment.class);
		ApartmentService.service.update(apartment);
		redirect("/admin/apartment");
	}
	
	/**
	 * 设置不可用
	 */
	public void off(){
		ApartmentService.service.off(Apartment.dao.findById(getPara("id")));
		redirect("/admin/apartment");
	}
	
	/**
	 * 设置可用
	 */
	public void on(){
		ApartmentService.service.on(Apartment.dao.findById(getPara("id")));
		redirect("/admin/apartment");
	}
	
	/**
	 * 设置是否可运维
	 */
	public void able(){
		Apartment apartment = Apartment.dao.findById(getPara("apartmentid"));
		boolean able =ApartmentService.service.operate(apartment);
		renderJson("state",able);
	}
	
	
}
