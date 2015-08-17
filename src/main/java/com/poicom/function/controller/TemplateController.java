package com.poicom.function.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.poicom.basic.kit.ObjectKit;
import com.poicom.function.model.Apartment;
import com.poicom.function.model.Etype;
import com.poicom.function.model.Template;
import com.poicom.function.model.User;
import com.poicom.function.model.UserInfo;
import com.poicom.function.service.ApartmentService;
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
			Apartment apartment = Apartment.dao.findById(userinfo.getLong("apartment_id"));
			String where = " and apartmentid = ? ";
			templatePage = TemplateService.service.allTemplatePage(getParaToInt(0,1), 10,where,apartment.getLong("pid"));
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
			Long rootApartment = getParaToLong("rootApartment[]");
			List<Apartment> childApartmentList=Apartment.dao.getATApartmentsList(rootApartment,childTypeid);
			renderJson("childApartmentList",childApartmentList);
		}else if(type.equals("recerver")){
			Integer[] selectApartment = getParaValuesToInt("selectApartment[]");
			if(selectApartment.length>1){
				Long childTypeid= getParaToLong("childTypeid");
				List<User> userList = new ArrayList<User>();
				for(int i=0;i<selectApartment.length;i++){
					int rootApartment=selectApartment[i];//一级部门id
					List<Apartment> childApartmentList=Apartment.dao.getATApartmentsList(rootApartment,childTypeid);
					List<User> users = ApartmentService.service.findUsers(childApartmentList);
					userList.addAll(users);
				}
				renderJson("userList",userList);
			}else{
				Long selectChildApartment=getParaToLong("selectChildApartment");
				List<User> userList = Apartment.dao.getUsersById(selectChildApartment);
				renderJson("userList",userList);
			}
			
		}
	}
	
	public void query(){
		Template template = Template.dao.findById(getParaToLong("id"));
		if(template == null ){
			
		}else {
			Apartment apartment = Apartment.dao.findById(template.getLong("apartmentid"));
			Etype etype = Etype.dao.findById(template.getLong("typeid"));
			List<User> userList = User.dao.findUserAttrValues(" * ",template.getStr("receive_userids"));
			ObjectKit.formatUser(userList);
			User created_user = User.dao.findById(template.getLong("created_userid"));
			Map<String,Object> attrMap = new HashMap<String,Object>();
			attrMap.put("template", template);
			attrMap.put("apartment", apartment);
			attrMap.put("etype", etype);
			attrMap.put("userList", userList);
			attrMap.put("created_user", created_user);
			
			setAttrs(attrMap);
		}
		
		render(TEMPLATE_QUERY_PAGE);
	}
	
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
		template.set("title", getPara("template.title"));
		template.set("context", getPara("template.context"));
		template.set("typeid", getParaToLong("template.typeid"));
		String[] receives = getParaValues("selectReceiver[]"); 
		TemplateService.service.update(template, receives);
		renderJson("state","提交成功");
	}
	
	
	public void delete(){}
	
	
	

}
