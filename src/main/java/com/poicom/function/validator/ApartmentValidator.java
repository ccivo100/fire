package com.poicom.function.validator;

import java.util.List;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.model.Apartment;

public class ApartmentValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		if(getActionKey().equals("/admin/apartment/save")){
			if(ValidateKit.isNullOrEmpty(c.getPara("apartment.name").trim())){
				addError("nameMsg", "部门名称不可为空");
			}
			List<Apartment> apartments=Apartment.dao.findApartmentsByPid(" pid=? ",0);
			
			if(ValidateKit.isNullOrEmpty(c.getPara("apartment.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				if(c.getParaToLong("apartment.pid")!=0){
					addError("pidMsg","父节点应该为0");
				}
			}else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				boolean flag=false;
				StringBuffer str=new StringBuffer();
				for(Apartment apartment:apartments){
					str.append(apartment.get("id")+" ");
					if(apartment.getLong("id")==c.getParaToLong("pid")){
						
						flag=true;
					}
				}
				if(!flag){
					addError("pidMsg","父节点应为："+str.toString());
				}
			}
			
		}
		//更新部门验证
		else if(getActionKey().equals("/admin/apartment/update")){
			if(ValidateKit.isNullOrEmpty(c.getPara("apartment.name").trim())){
				addError("nameMsg", "部门名称不可为空");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("apartment.pid").trim())){
				addError("pidMsg","父节点不可为空");
				
			}else{
				Apartment apartment=Apartment.dao.findById(c.getParaToInt("apartment.id"));
				if(apartment.getLong("pid")==0&c.getParaToLong("apartment.pid")!=apartment.getLong("pid")){
					addError("pidMsg","根节点不可修改");
				}else if(apartment.getLong("pid")!=0&c.getParaToLong("apartment.pid")==0){
					addError("pidMsg","子节点不可修改为根节点");
				}
				else if(c.getParaToLong("apartment.pid")!=0){
					List<Apartment> apartments=Apartment.dao.findApartmentsByPid(" pid=? ",0);
					boolean flag=false;
					StringBuffer str=new StringBuffer();
					for(Apartment a:apartments){
						str.append(a.get("id")+" ");
						if(c.getParaToLong("apartment.pid")==a.getLong("id")){
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
		if(getActionKey().equals("/admin/apartment/save")){
			if(ValidateKit.isNullOrEmpty(c.getPara("apartment.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("apartment.pid");
			}else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("pid");
			}
			c.render("/app/admin/apartment/add.html");
		}else if(getActionKey().equals("/admin/apartment/update")){
			c.keepModel(Apartment.class);
			c.render("/app/admin/apartment/edit.html");
		}

	}

}
