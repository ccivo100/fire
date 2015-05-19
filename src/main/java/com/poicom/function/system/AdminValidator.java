package com.poicom.function.system;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;
import cn.dreampie.shiro.core.SubjectKit;
import cn.dreampie.shiro.hasher.Hasher;
import cn.dreampie.shiro.hasher.HasherKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.common.kit.ValiKit;
import com.poicom.function.app.model.Apartment;
import com.poicom.function.app.model.Branch;
import com.poicom.function.app.model.ErrorType;
import com.poicom.function.app.model.Position;
import com.poicom.function.system.model.Permission;
import com.poicom.function.system.model.Role;
import com.poicom.function.system.model.User;

/**
 * 
 * @author 唐东宇
 *
 */
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
		//新增用户验证
		else if(getActionKey().equals("/admin/doadduser")){
			User user=User.dao.findFirstBy("username=?", c.getPara("user.username").trim());
			if(!ValidateKit.isGeneral(c.getPara("user.username"), 2, 16)){
				addError("usernameMsg","用户名为英文字母 、数字和下划线，2~16个字符！");
			}
			if(!ValidateKit.isNullOrEmpty(user)){
				addError("usernameMsg","该用户名已存在！");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("user.first_name"))||ValidateKit.isNullOrEmpty("user.last_name")){
				addError("fullnameMsg","姓名不能为空！");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("selectGender"))){
				addError("genderMsg","性别不能为空！");
			}
			if(!ValiKit.isPhone(c.getPara("user.phone"))){
				addError("phoneMsg","联系方式格式不正确！");
			}
			if(!ValidateKit.isEmail(c.getPara("user.email"))){
				addError("emailMsg","邮箱格式不正确！");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("selectBranch"))){
				
				if(SubjectKit.getSubject().hasRole("R_ADMIN")){
					addError("branchMsg","单位不能为空！");
				}else{
					
				}
				
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("selectApartment"))){
				addError("apartmentMsg","部门不能为空！");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("selectPosition"))){
				addError("positionMsg","性别不能为空！");
			}
		}
		
		//故障类型验证
		else if(getActionKey().equals("/admin/doaddtype")|getActionKey().equals("/admin/doedittype")){
			if(ValidateKit.isNullOrEmpty("errorType.name")){
				
				addError("nameMsg", "输入内容不为空");
			}else if(!ValidateKit.isLength(c.getPara("errorType.name"), 5, 15)){
				addError("nameMsg", "输入字数不少于5...");
			}
		}
		//单位验证
		else if(getActionKey().equals("/admin/doaddbranch")|getActionKey().equals("/admin/doeditbranch")){
			if(ValidateKit.isNullOrEmpty("branch.name")){
				
				addError("nameMsg", "输入内容不为空");
			}else if(!ValidateKit.isLength(c.getPara("branch.name"), 4, 15)){
				addError("nameMsg", "输入字数不少于4...");
			}
		}
		
		/*else if(getActionKey().equals("/admin/doaddapartment")|getActionKey().equals("/admin/doeditapartment")){
			if(ValidateKit.isNullOrEmpty("apartment.name")){
				
				addError("nameMsg", "输入内容不为空");
			}else if(!ValidateKit.isLength(c.getPara("apartment.name"), 3, 15)){
				addError("nameMsg", "输入字数不少于3...");
			}
		}*/
		//新增部门验证
		else if(getActionKey().equals("/admin/doaddapartment")){
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
		else if(getActionKey().equals("/admin/doeditapartment")){
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
		//职位验证
		else if(getActionKey().equals("/admin/doaddposition")|getActionKey().equals("/admin/doeditposition")){
			if(ValidateKit.isNullOrEmpty("position.name")){
				
				addError("nameMsg", "输入内容不为空");
			}else if(!ValidateKit.isLength(c.getPara("position.name"), 3, 15)){
				addError("nameMsg", "输入字数不少于3...");
			}
		}
		//修改密码
		else if(getActionKey().equals("/admin/updatePwd")){
			
			boolean oldpasswordEmpty=ValidateKit.isNullOrEmpty(c.getPara("oldpassword"));
			
			boolean passwordEmpty=ValidateKit.isNullOrEmpty(c.getPara("user.password"));
			
			
			//新密码不为空
			if (passwordEmpty) {
				addError("user_passwordMsg", "新密码不能为空");
			}
			//新密码格式
			else if (!passwordEmpty&&!ValidateKit.isPassword(c.getPara("user.password"))) {
				addError("user_passwordMsg", "新密码为英文字母 、数字和下划线长度为5-18");
			}
			//新密码与重复密码
			else if (!passwordEmpty&&!c.getPara("user.password").equals(c.getPara("repassword"))) {
				addError("user_passwordMsg", "重复密码不匹配");
			}
			
			//旧密码不为空
			else if(oldpasswordEmpty){
				addError("user_passwordMsg", "原始密码不能为空！");
			}
			
			else if (!oldpasswordEmpty) {
				User user=User.dao.findById(SubjectKit.getUser().get("id"));
				if (user!=null) {
					boolean match=HasherKit.match(c.getPara("oldpassword"), user.getStr("password"),Hasher.DEFAULT);
					if (!match) {
						addError("user_passwordMsg", "原始密码不匹配");
					}
				}else {
					addError("user_passwordMsg", "用户信息错误！");
				}
				
				
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
			c.render("/app/admin/role/add.html");
		}else if(getActionKey().equals("/admin/doedit")){
			c.keepModel(Role.class);
			c.render("/app/admin/role/edit.html");
		}
		
		else if(getActionKey().equals("/admin/doaddpermission")){
			if(ValidateKit.isNullOrEmpty(c.getPara("permission.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("permission.pid");
			}else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("pid");
			}
			c.render("/app/admin/permission/add.html");
		}else if(getActionKey().equals("/admin/doeditpermission")){
			c.keepModel(Permission.class);
			c.render("/app/admin/permission/edit.html");
		}
		//新增用户跳转
		else if(getActionKey().equals("/admin/doadduser")){
			c.setAttr("branchList",Branch.dao.getAllBranch());
			c.setAttr("apartmentList",Apartment.dao.getAllApartment());
			c.setAttr("positionList",Position.dao.getAllPosition());
			c.keepModel(User.class);
			c.keepPara("branchid");
			c.render("/app/admin/user/add.html");
			
			
		}
		//故障类型跳转
		else if(getActionKey().equals("/admin/doaddtype")){
			c.keepModel(ErrorType.class);
			c.render("/app/admin/type/add.html");
		}else if(getActionKey().equals("/admin/doedittype")){
			c.keepModel(ErrorType.class);
			c.render("/app/admin/type/edit.html");
		}
		//单位跳转
		else if(getActionKey().equals("/admin/doaddbranch")){
			c.keepModel(Branch.class);
			c.render("/app/admin/branch/add.html");
		}else if(getActionKey().equals("/admin/doeditbranch")){
			c.keepModel(Branch.class);
			c.render("/app/admin/branch/edit.html");
		}
		//部门跳转
		else if(getActionKey().equals("/admin/doaddapartment")){
			if(ValidateKit.isNullOrEmpty(c.getPara("apartment.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("apartment.pid");
			}else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("pid");
			}
			c.render("/app/admin/apartment/add.html");
		}else if(getActionKey().equals("/admin/doeditapartment")){
			c.keepModel(Apartment.class);
			c.render("/app/admin/apartment/edit.html");
		}
		//职位跳转
		else if(getActionKey().equals("/admin/doaddposition")){
			c.keepModel(Position.class);
			c.render("/app/admin/position/add.html");
		}else if(getActionKey().equals("/admin/doeditposition")){
			c.keepModel(Position.class);
			c.render("/app/admin/position/edit.html");
		}
		else if(getActionKey().equals("/admin/updatePwd")){
			c.keepModel(User.class);
			c.keepPara();
			c.render("/app/admin/center/pwd.html");
		}
	}

}
