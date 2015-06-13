package com.poicom.function.validator;

import java.util.List;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.model.Permission;

public class PermissionValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		
		//新增权限验证
		if(getActionKey().equals("/admin/permission/save")){
			if(ValidateKit.isNullOrEmpty(c.getPara("permission.name").trim())){
				addError("nameMsg", "权限名称不可为空");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("permission.value").trim())){
				addError("valueMsg","权限代码不可为空");
			}
			List<Permission> permissions=Permission.dao.findPermissionsByPid(" pid=?",0);
			
			if(ValidateKit.isNullOrEmpty(c.getPara("permission.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				if(c.getParaToLong("permission.pid")!=0){
					addError("pidMsg","父节点应该为0");
				}
			}else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				boolean flag=false;
				StringBuffer str=new StringBuffer();
				for(Permission permission:permissions){
					str.append(permission.get("id")+" ");
					if(permission.getLong("id")==c.getParaToLong("pid")){
						
						flag=true;
					}
				}
				if(!flag){
					addError("pidMsg","父节点应为："+str.toString());
				}
			}
			
		}
		//更新权限验证
		else if(getActionKey().equals("/admin/permission/update")){
			if(ValidateKit.isNullOrEmpty(c.getPara("permission.name").trim())){
				addError("nameMsg", "权限名称不可为空");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("permission.value").trim())){
				addError("valueMsg","权限代码不可为空");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("permission.pid").trim())){
				addError("pidMsg","父节点不可为空");
				
			}else{
				Permission permission=Permission.dao.findById(c.getParaToInt("permission.id"));
				if(permission.getLong("pid")==0&c.getParaToLong("permission.pid")!=permission.getLong("pid")){
					addError("pidMsg","根节点不可修改");
				}
				else if(permission.getLong("pid")!=0&c.getParaToLong("permission.pid")==0){
					addError("pidMsg","子节点不可修改为根节点");
				}
				else if(c.getParaToLong("permission.pid")!=0){
					List<Permission> permissions=Permission.dao.findPermissionsByPid(" pid=?",0);
					boolean flag=false;
					StringBuffer str=new StringBuffer();
					for(Permission p:permissions){
						str.append(p.get("id")+" ");
						if(c.getParaToLong("permission.pid")==p.getLong("id")){
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
		if(getActionKey().equals("/admin/permission/save")){
			if(ValidateKit.isNullOrEmpty(c.getPara("permission.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("permission.pid");
			}else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("pid");
			}
			c.render("/app/admin/permission/add.html");
		}else if(getActionKey().equals("/admin/permission/update")){
			c.keepModel(Permission.class);
			c.render("/app/admin/permission/edit.html");
		}

	}

}
