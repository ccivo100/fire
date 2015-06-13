package com.poicom.function.validator;

import java.util.List;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.jfinal.validate.Validator;
import com.poicom.function.model.Role;

public class RoleValidator extends Validator{
	
	private static Logger log = Logger.getLogger(RoleValidator.class);

	@Override
	protected void validate(Controller c) {
		//新增角色验证
		if(getActionKey().equals("/admin/role/save")){
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
		else if(getActionKey().equals("/admin/role/update")){
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
	}

	@Override
	protected void handleError(Controller c) {
		if(getActionKey().equals("/admin/role/save")){
			if(ValidateKit.isNullOrEmpty(c.getPara("role.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("role.pid");
			}else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("pid");
			}
			c.render("/app/admin/role/add.html");
		}else if(getActionKey().equals("/admin/role/update")){
			c.keepModel(Role.class);
			c.render("/app/admin/role/edit.html");
		}
	}

}
