package com.poicom.function.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.poicom.function.model.Apartment;
import com.poicom.function.model.Etype;
import com.poicom.function.model.Template;
import com.poicom.function.model.User;
import com.poicom.function.model.UserInfo;
import com.poicom.function.service.TemplateService;
import com.poicom.function.validator.TemplateValidator;

import cn.dreampie.routebind.ControllerKey;
import cn.dreampie.shiro.core.SubjectKit;

@ControllerKey(value = "/admin/template", path="/app/admin")
public class TemplateController extends BaseController {
	
	private static Logger log = Logger.getLogger(TemplateController.class);
	
	private final static String TEMPLATE_INDEX_PAGE= "template/index.html";
	private final static String TEMPLATE_ADD_PAGE="template/add.html";
	private final static String TEMPLATE_EDIT_PAGE="template/edit.html";
	private final static String TEMPLATE_QUERY_PAGE="template/query.html";
	
	public void index(){
		Page<Template> templatePage ;
		if(SubjectKit.getSubject().hasRole("R_ADMIN")) {
			templatePage = TemplateService.service.allTemplatePage(getParaToInt(0,1), 10, "");
		}else {
			User currentUser = SubjectKit.getUser();
			UserInfo userinfo = currentUser.getUserInfo();
			String where = " and apartmentid = ? ";
			templatePage = TemplateService.service.allTemplatePage(getParaToInt(0,1), 10,where,userinfo.getLong("apartment_id"));
		}
		
		setAttr("templatePage", templatePage);
		
		render(TEMPLATE_INDEX_PAGE);
	}
	
	public void handler(){
		String type = getPara("type");
		if(type.equals("type")){
			List<Etype> typeList=Etype.dao.rootNode(" pid=? and deleted_at is null ", 0);
			renderJson("typeList", typeList);
		}else if(type.equals("apartment")){
			Long typeid= getParaToLong("typeid");
			Map<String,Object> jsonList= new HashMap<String,Object>();
			List<Apartment> apartmentList=Apartment.dao.getApartmentsList(typeid);
			jsonList.put("apartmentList", apartmentList);//一级部门
			
			List<Etype> childTypeList = Etype.dao.childNode(" pid=? and deleted_at is null ", typeid);
			jsonList.put("childTypeList", childTypeList);//故障子类
			renderJson(jsonList);
			
		}else if(type.equals("childApartment")){
			Long childTypeid= getParaToLong("childTypeid");
			Long rootApartment = getParaToLong("rootApartment");
			List<Apartment> childApartmentList=Apartment.dao.getATApartmentsList(rootApartment,childTypeid);
			renderJson("childApartmentList",childApartmentList);
		}else if(type.equals("recerver")){
			Long selectChildApartment=getParaToLong("selectChildApartment");
			List<User> userList = Apartment.dao.getUsersById(selectChildApartment);
			renderJson("userList",userList);
		}
	}
	
	public void query(){}
	
	public void add(){
		
		render(TEMPLATE_ADD_PAGE);
	}
	
	@Before({TemplateValidator.class,Tx.class})
	public void save(){
		
		Template template = getModel(Template.class);
		String[] receives = getParaValues("selectReceiver[]"); 
		TemplateService.service.save(template, receives);
		renderJson("state","提交成功");
	}
	
	public void edit(){
		
		Template template = Template.dao.findById(getParaToLong("id"));
		setAttr("template", template);
		render(TEMPLATE_EDIT_PAGE);
	}
	
	@Before({TemplateValidator.class,Tx.class})
	public void update(){
		Template template = Template.dao.findById(getParaToLong("template.id"));
		template.set("context", getPara("template.context"));
		String[] receives = getParaValues("selectReceiver[]"); 
		TemplateService.service.update(template, receives);
		renderJson("state","提交成功");
	}
	
	
	public void delete(){}
	
	
	

}
