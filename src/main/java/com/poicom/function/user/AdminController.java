package com.poicom.function.user;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;
import cn.dreampie.quartz.QuartzKey;
import cn.dreampie.quartz.job.QuartzCronJob;
import cn.dreampie.quartz.job.QuartzOnceJob;
import cn.dreampie.routebind.ControllerKey;
import cn.dreampie.shiro.core.SubjectKit;
import cn.dreampie.shiro.hasher.HasherInfo;
import cn.dreampie.shiro.hasher.HasherKit;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.poicom.function.app.model.ErrorType;
import com.poicom.function.app.model.Order;
import com.poicom.function.job.AlertJob;
import com.poicom.function.user.model.Permission;
import com.poicom.function.user.model.Role;
import com.poicom.function.user.model.RolePermission;
import com.poicom.function.user.model.User;
import com.poicom.function.user.model.UserInfo;
import com.poicom.function.user.model.UserRole;

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
	
	/**
	 * 角色分配权限
	 */
	public void assignrole(){
		
		Role role=Role.dao.findById(getPara("id"));
		List<String> userpermission=RolePermission.dao.findPermissionIds("role_id=?",role.get("id"));
		List<Permission> permissions=Permission.dao.findPermissionsByPid(0);
		for(Permission permission:permissions){
			List<Permission> pchild=Permission.dao.findPermissionsByPid(permission.get("id"));
			permission.setChildren(pchild);
		}
		
		setAttr("role",role);
		setAttr("userpermission",userpermission);
		setAttr("permissions",permissions);
		render("/page/app/admin/role/assign.html");
	}
	
	@Before(Tx.class)
	public void doassignrole(){
		//页面传值
		String[] permissions=getParaValues("pers");
		Long roleid=getParaToLong("roleid");
		//当前角色已分配权限List
		List<RolePermission> rps=RolePermission.dao.findRolePermissionByRoleId(roleid);
		
		if(ValidateKit.isNullOrEmpty(permissions)){
			for(int i=0;i<rps.size();i++){
				logger.info(rps.get(i).getLong("permission_id")+" 已取消，执行删除操作！");
				rps.get(i).delete();
			}
		}else {
			Map<Long,Permission> perMap=new HashMap<Long,Permission>();
			for(int i=0;i<permissions.length;i++){
				//根据页面选择的id，查询该Permission的父id，根据父id查询到父Permission。
				Permission pf=Permission.dao.findById(Permission.dao.findById(permissions[i]).get("pid"));
				if(perMap.get(pf.get("id"))==null){
					perMap.put(pf.getLong("id"), pf);
				}
				new RolePermission().set("role_id", roleid).set("permission_id", permissions[i]).save();
			}
			for(RolePermission rp:rps){
				rp.delete();
			}
			for(Permission p:perMap.values()){
				new RolePermission().set("role_id", roleid).set("permission_id", p.get("id")).save();
			}
		}
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
	
	public void loadPermission(){
		int id=getParaToInt("rid");
		List<Permission> permissionList=Permission.dao.findByRole("",id);
		
		renderJson("permissionList",permissionList);
		//render("/page/app/admin/role/loadper.html");
	}
	
	
	
	/**
	 * 用户管理
	 */
	public void user(){
		Page<User> userPage=User.dao.getAllUserPage(getParaToInt(0,1), 10);
		
		for(int i=0;i<userPage.getList().size();i++){
			List<Record> list=UserRole.dao.findUserRolesById(userPage.getList().get(i).get("uuserid"));
			StringBuffer roles=new StringBuffer();
			for(int j=0;j<list.size();j++){
				roles.append(list.get(j).get("rname")+"；");
			}
			userPage.getList().get(i).set("remark", roles);
			
		}
		setAttr("userPage",userPage);
	}
	
	public void edituser(){
		String userid=getPara("id");
		setAttr("userinfo",UserInfo.dao.getAllUserInfo(userid));
		setAttr("userrole",UserRole.dao.findUserRolesById(userid));
		setAttr("roleList",Role.dao.findAll());
		render("/page/app/admin/user/edit.html");
	}
	/**
	 * @描述 执行分配角色操作
	 */
	@Before(Tx.class)
	public void doedituser(){
		//前台选中的 角色s
		String[] roles=getParaValues("roles");
		
		//用户-角色 中间表
		List<Record> sur=UserRole.dao.findUserRolesById(getParaToLong("userid"));
		
		//1、用户有角色，取消所有角色
		if(ValidateKit.isNullOrEmpty(roles)){
			for(int i=0;i<sur.size();i++){
				logger.info(sur.get(i).getLong("roleid")+" 已取消，执行删除操作！");
				UserRole.dao.findById(sur.get(i).getLong("id")).delete();
			}
		}
		//2、用户无角色，新增角色
		else if(ValidateKit.isNullOrEmpty(sur)){
			for(int i=0;i<roles.length;i++){
				logger.info(roles[i]+" 不存在，执行新增操作！");
				new UserRole().set("user_id", getPara("userid")).set("role_id", roles[i]).save();
			}
		}
		//3、用户有角色
		else{
			//需要分配该角色?，不存在则新增。
			for(int i=0;i<roles.length;i++){
				boolean flag=false;
				for(int j=0;j<sur.size();j++){
					if(Integer.parseInt(roles[i])==sur.get(j).getLong("roleid")){
						flag=true;
						break;
					}
				}
				if(flag){
					logger.info(roles[i]+" 存在，保留不删除！");
				}else if(!flag){
					logger.info(roles[i]+" 不存在，执行新增操作！");
					new UserRole().set("user_id", getPara("userid")).set("role_id", roles[i]).save();
				}
			}
			
			//需要保留该角色?，不保留则删除
			for(int i=0;i<sur.size();i++){
				boolean flag=false;
				for(int j=0;j<roles.length;j++){
					if(sur.get(i).getLong("roleid")==Integer.parseInt(roles[j])){
						flag=true;
						break;
					}
				}
				if(flag){
					logger.info(sur.get(i).getLong("roleid")+" 未取消，保留不删除！");
				}else if(!flag){
					logger.info(sur.get(i).getLong("roleid")+" 已取消，执行删除操作！");
					UserRole.dao.findById(sur.get(i).getLong("id")).delete();
				}
			}
		}
		redirect("/admin/user");
	}
	
	public void onuser(){
		User.dao.findById(getPara("id")).set("deleted_at", null).update();
		redirect("/admin/user");
	}
	public void offuser(){
		User.dao.findById(getPara("id")).set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		redirect("/admin/user");
	}
	
	/**
	 * 工单管理
	 */
	public void exception(){
		String where=" o.status=2 OR o.spend_time IS NOT NULL";
		Page<Record> orderPage=Order.dao.findExceptionOrders(getParaToInt(0,1), 10,where);
		setAttr("orderPage",orderPage);
		
	}
	
	/**
	 * 故障类型管理
	 */
	public void type(){
		List<ErrorType> typeList=ErrorType.dao.findAll();
		setAttr("typeList",typeList);
	}
	
	public void addtype(){
		render("/page/app/admin/type/add.html");
	}
	@Before(AdminValidator.class)
	public void doaddtype(){
		getModel(ErrorType.class).save();
		redirect("/admin/type");
	}
	public void edittype(){
		ErrorType errorType=ErrorType.dao.findById(getPara("id"));
		setAttr("errorType",errorType);
		render("/page/app/admin/type/edit.html");
	}
	@Before(AdminValidator.class)
	public void doedittype(){
		getModel(ErrorType.class).update();
		redirect("/admin/type");
	}
	public void ontype(){
		ErrorType.dao.findById(getPara("id")).set("deleted_at", null).update();
		redirect("/admin/type");
	}
	public void offtype(){
		ErrorType.dao.findById(getPara("id")).set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		redirect("/admin/type");
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
