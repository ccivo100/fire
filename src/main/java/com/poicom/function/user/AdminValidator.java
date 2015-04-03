package com.poicom.function.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.app.model.ErrorType;
import com.poicom.function.user.model.Permission;
import com.poicom.function.user.model.Role;

public class AdminValidator extends Validator{
	
	protected Logger logger=LoggerFactory.getLogger(getClass());

	@Override
	protected void validate(Controller c) {
		
		//新增角色验证
		if(getActionKey().equals("/admin/doadd")){
			if(ValidateKit.isNullOrEmpty(c.getPara("role.name").trim())){
				addError("nameMsg", "角色名称不可为空");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("role.value").trim())){
				addError("valueMsg","角色代码不可为空");
			}
			List<Role> roles=Role.dao.findRolesByPid(0);
			
			if(ValidateKit.isNullOrEmpty(c.getPara("role.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				if(c.getParaToLong("role.pid")!=0){
					addError("pidMsg","父节点应该为0");
				}
			}else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				boolean flag=false;
				StringBuffer str=new StringBuffer();
				for(Role role:roles){
					str.append(role.get("id")+" ");
					if(role.getLong("id")==c.getParaToLong("pid")){
						
						flag=true;
					}
				}
				if(!flag){
					addError("pidMsg","父节点应为："+str.toString());
				}
			}
			
		}
		//更新角色验证
		else if(getActionKey().equals("/admin/doedit")){
			if(ValidateKit.isNullOrEmpty(c.getPara("role.name").trim())){
				addError("nameMsg", "角色名称不可为空");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("role.value").trim())){
				addError("valueMsg","角色代码不可为空");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("role.pid").trim())){
				addError("pidMsg","父节点不可为空");
				
			}else{
				Role role=Role.dao.findById(c.getParaToInt("role.id"));
				if(role.getLong("pid")==0&c.getParaToLong("role.pid")!=role.getLong("pid")){
					addError("pidMsg","根节点不可修改");
				}else if(role.getLong("pid")!=0&c.getParaToLong("role.pid")==0){
					addError("pidMsg","子节点不可修改为根节点");
				}
				else if(c.getParaToLong("role.pid")!=0){
					List<Role> roles=Role.dao.findRolesByPid(0);
					boolean flag=false;
					StringBuffer str=new StringBuffer();
					for(Role r:roles){
						str.append(r.get("id")+" ");
						if(c.getParaToLong("role.pid")==r.getLong("id")){
							flag=true;
						}
					}
					if(!flag){
						addError("pidMsg","父节点应为："+str.toString());
					}
				}
			}
		}
		//新增权限验证
		else if(getActionKey().equals("/admin/doaddpermission")){
			if(ValidateKit.isNullOrEmpty(c.getPara("permission.name").trim())){
				addError("nameMsg", "权限名称不可为空");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("permission.value").trim())){
				addError("valueMsg","权限代码不可为空");
			}
			List<Permission> permissions=Permission.dao.findPermissionsByPid(0);
			
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
		else if(getActionKey().equals("/admin/doeditpermission")){
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
					List<Permission> permissions=Permission.dao.findPermissionsByPid(0);
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
		
		//
		else if(getActionKey().equals("/admin/doaddtype")|getActionKey().equals("/admin/doedittype")){
			if(ValidateKit.isNullOrEmpty("errorType.name")){
				
				addError("nameMsg", "输入内容不为空");
			}else if(!ValidateKit.isLength(c.getPara("errorType.name"), 5, 15)){
				addError("nameMsg", "输入字数不少于5...");
			}
		}

	}

	@Override
	protected void handleError(Controller c) {
		
		if(getActionKey().equals("/admin/doadd")){
			if(ValidateKit.isNullOrEmpty(c.getPara("role.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("role.pid");
			}else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("pid");
			}
			c.render("/page/app/admin/role/add.html");
		}else if(getActionKey().equals("/admin/doedit")){
			c.keepModel(Role.class);
			c.render("/page/app/admin/role/edit.html");
		}
		
		else if(getActionKey().equals("/admin/doaddpermission")){
			if(ValidateKit.isNullOrEmpty(c.getPara("permission.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("permission.pid");
			}else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("pid");
			}
			c.render("/page/app/admin/permission/add.html");
		}else if(getActionKey().equals("/admin/doeditpermission")){
			c.keepModel(Permission.class);
			c.render("/page/app/admin/permission/edit.html");
		}
		else if(getActionKey().equals("/admin/doaddtype")){
			c.keepModel(ErrorType.class);
			c.render("/page/app/admin/type/add.html");
		}else if(getActionKey().equals("/admin/doedittype")){
			c.keepModel(ErrorType.class);
			c.render("/page/app/admin/type/edit.html");
		}
	}

}
