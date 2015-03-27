package com.poicom.function.user;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;
import cn.dreampie.shiro.core.SubjectKit;
import cn.dreampie.shiro.hasher.HasherInfo;
import cn.dreampie.shiro.hasher.HasherKit;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.poicom.function.user.model.Permission;
import com.poicom.function.user.model.Role;
import com.poicom.function.user.model.User;

@ControllerKey(value="/admin",path="/page/app/admin")
public class AdminController extends Controller {
	
	protected Logger logger=LoggerFactory.getLogger(getClass());
	
	public void index(){
		setAttr("userPage", User.dao.getUserPage(getParaToInt(0, 1), 10));
		render("user.html");
	}
	
	/**
	 * 角色管理
	 */
	public void role(){
		List<Role> roles=Role.dao.findRolesByPid(0);
		for(Role role:roles){
			List<Role> rchild=Role.dao.findRolesByPid(role.get("id"));
			role.setChildren(rchild);
		}
		setAttr("roleTree",roles);
		
	}
	
	/**
	 * 新增角色
	 */
	public void addrole(){
		
		if(ValidateKit.isNullOrEmpty(getPara("id"))){

		}else{
			System.out.println(getParaToLong("id"));
			setAttr("pid",getParaToLong("id"));
		}
		
		render("/page/app/admin/role/add.html");
	}
	/**
	 * 新增角色操作
	 */
	@Before(AdminValidator.class)
	public void doadd(){
		getModel(Role.class).save();
		redirect("/admin/role");
	}
	
	/**
	 * 更新角色
	 */
	public void editrole(){
		Role role=Role.dao.findById(getPara("id"));
		setAttr("role",role);
		render("/page/app/admin/role/edit.html");
	}
	/**
	 * 更新角色操作
	 */
	@Before(AdminValidator.class)
	public void doedit(){
		getModel(Role.class).update();
		redirect("/admin/role");
	}
	
	public void offrole(){
		Role.dao.findById(getPara("id")).set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		redirect("/admin/role");
	}
	
	public void onrole(){
		Role.dao.findById(getPara("id")).set("deleted_at", null).update();
		redirect("/admin/role");
	}
	
	
	/**
	 * 权限管理
	 */
	public void permission(){
		List<Permission> permissions=Permission.dao.findPermissionsByPid(0);
		for(Permission permission:permissions){
			List<Permission> pchild=Permission.dao.findPermissionsByPid(permission.get("id"));
			permission.setChildren(pchild);
		}
		setAttr("permissionTree",permissions);
	}
	/**
	 * 新增权限
	 */
	public void addpermission(){
		if(ValidateKit.isNullOrEmpty(getPara("id"))){

		}else{
			System.out.println(getParaToLong("id"));
			setAttr("pid",getParaToLong("id"));
		}
		render("/page/app/admin/permission/add.html");
	}
	/**
	 * 新增权限操作
	 */
	@Before(AdminValidator.class)
	public void doaddpermission(){
		getModel(Permission.class).save();
		redirect("/admin/permission");
	}
	
	/**
	 * 更新权限
	 */
	public void editpermission(){
		Permission permission=Permission.dao.findById(getPara("id"));
		setAttr("permission",permission);
		render("/page/app/admin/permission/edit.html");
	}
	/**
	 * 更新权限操作
	 */
	@Before(AdminValidator.class)
	public void doeditpermission(){
		getModel(Permission.class).update();
		redirect("/admin/permission");
	}
	
	public void offpermission(){
		Permission.dao.findById(getPara("id")).set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		redirect("/admin/permission");
	}
	
	public void onpermission(){
		Permission.dao.findById(getPara("id")).set("deleted_at", null).update();
		redirect("/admin/permission");
	}
	
	
	
	/**
	 * 用户管理
	 */
	public void user(){
		
	}
	
	/**
	 * 工单管理
	 */
	public void order(){
		
	}
	
	
	public void add(){}
	
	/**
	 * @description 添加新用户
	 * 				添加sec_user基础数据
	 * 				添加sec_user_role用户角色
	 * 				添加sec_user_info用户拓展数据
	 */
	@Before(Tx.class)
	public void control(){
		
		keepModel(User.class);
		User user=getModel(User.class);
		User subUser=SubjectKit.getUser();
		boolean result=false;
		String todo=getPara("do");
		System.out.println(todo);
		if(todo!=null){
			if(todo.equals("delete")&&user.getLong("id")!=null){
				result = user.set("deleted_at", new Timestamp(new Date().getTime())).update();
			}else if(todo.equals("save")&&user.getLong("id")!=null){
				HasherInfo hasher = HasherKit.hash(user.getStr("password"));
				if(user.getStr("first_name")==null)
					user.set("first_name", "三角").set("last_name", "中心");
				
				user.set("password", hasher.getHashResult())
				.set("salt", hasher.getSalt())
				.set("hasher", hasher.getHasher().value())
				.set("providername", subUser.getStr("full_name"))
				.set("full_name", user.getStr("last_name") + "." + user.getStr("first_name"));
				result=user.save();
			}else if(todo.equals("update")&&user.getLong("id")!=null){
				
			}
		}
		
		
		/*if(todo!=null){
			if(todo.equals("delete")&&(user.getLong("id")!=null)){
				user.set("deleted_at", new Timestamp(new Date().getTime())).update();
			}else if(todo.equals("update")&&user.getLong("id")!=null){
				user.removeNullValueAttrs();
				Boolean reuse = getParaToBoolean("reuse");
				if(reuse!=null&&reuse)
					user.set("deleted_at", null);
				if(user.getBoolean("last_name")!=null)
					user.set("full_name", user.get("last_name")+"."+user.get("first_name"));
				result=user.update();
			}
		}*/
		
		if (result) {
			setAttr("state", "success");
			redirect("/admin");
		} else{
			setAttr("state", "failure");
			redirect("/admin");
		}
	}
	
	public void edit(){
		User user=User.dao.findById(getParaToInt());
		HasherInfo passwordInfo =HasherKit.hash(user.getStr("password"));
		setAttr("user", User.dao.findById(getParaToInt()));
	}
	
	public void update(){
		
	}
	
	public void query(){
		
	}
	

}
