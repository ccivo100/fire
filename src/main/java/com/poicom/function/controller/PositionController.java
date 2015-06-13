package com.poicom.function.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.poicom.function.model.Apartment;
import com.poicom.function.model.Position;
import com.poicom.function.service.ApartmentService;
import com.poicom.function.service.PositionService;
import com.poicom.function.validator.PositionValidator;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;

@ControllerKey(value = "/admin/position", path="/app/admin")
public class PositionController extends BaseController {
	
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(PositionController.class);
	
	private final static String POSITION_ADD_PAGE="position/add.html";
	private final static String POSITION_EDIT_PAGE="position/edit.html";
	
	public void index(){
		Page<Position> positionPage;
		String orderby=" ORDER BY position.id ";
		if(ValidateKit.isNullOrEmpty(getPara("position"))){
			String where = " 1=1 ";
			positionPage=Position.dao.findPositionPage(getParaToInt(0,1), 10, where, orderby);
		}else{
			String where=" position.name like ?  ";
			String condition ="%"+getPara("position").trim()+"%";
			positionPage=Position.dao.findPositionPage(getParaToInt(0,1), 10, where, orderby, condition);
			setAttr("position",getPara("position").trim());
		}
		setAttr("positionPage",positionPage);
		render("position.html");
	}
	
	/**
	 * 准备新增职位
	 */
	public void add(){
		List<Apartment> rootApartment=ApartmentService.service.rootNode();
		setAttr("rootApartment",rootApartment);
		render(POSITION_ADD_PAGE);
	}
	
	/**
	 * 新增职位
	 */
	@Before(PositionValidator.class)
	public void save(){
		Position position=getModel(Position.class);
		
		PositionService.service.save(position);
		redirect("/admin/position");
	}
	
	/**
	 * 准备更新职位
	 */
	public void edit(){
		Position position=Position.dao.findById(getPara("id"));
		List<Apartment> rootApartment=ApartmentService.service.rootNode();
		setAttr("rootApartment",rootApartment);
		setAttr("position",position);
		render(POSITION_EDIT_PAGE);
	}
	
	/**
	 * 更新职位
	 */
	@Before(PositionValidator.class)
	public void update(){
		Position position=getModel(Position.class);
		PositionService.service.update(position);
		redirect("/admin/position");
	}
	
	/**
	 * 设置不可用
	 */
	public void off(){
		PositionService.service.off(Position.dao.findById(getPara("id")));
		redirect("/admin/position");
	}
	
	/**
	 * 设置可用
	 */
	public void on(){
		PositionService.service.on(Position.dao.findById(getPara("id")));
		redirect("/admin/position");
	}

}
