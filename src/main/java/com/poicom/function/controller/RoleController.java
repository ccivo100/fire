package com.poicom.function.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;
import com.poicom.basic.kit.WebKit;
import com.poicom.function.model.Permission;
import com.poicom.function.model.Role;
import com.poicom.function.model.RolePermission;
import com.poicom.function.service.RoleService;
import com.poicom.function.validator.RoleValidator;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;

/**
 * 角色控制器
 * @author FireTercel 2015年6月5日 
 *
 */
@ControllerKey(value = "/admin/role", path="/app/admin")
public class RoleController extends BaseController {
	
	private static Logger log = Logger.getLogger(RoleController.class);
	
	private final static String ROLE_ADD_PAGE="role/add.html";
	private final static String ROLE_EDIT_PAGE="role/edit.html";
	private final static String ROLE_ASSIGN_PAGE="role/assign.ftl";
	
	/**
	 * 角色列表
	 */
	@Before(CacheInterceptor.class)
	@CacheName("/admin/role")
	public void index(){
		List<Role> roles=Role.dao.findRolesByPid(0);
		for(Role role:roles){
			List<Role> rchild=Role.dao.findRolesByPid(role.get("id"));
			role.setChildren(rchild);
		}
		
		setAttr("roleTree",roles);
		render("role.html");
	}
	
	/**
	 * 准备新增角色
	 */
	public void add(){
		if(ValidateKit.isNullOrEmpty(getPara("id"))){

		}else{
			System.out.println(getParaToLong("id"));
			setAttr("pid",getParaToLong("id"));
		}
		render(ROLE_ADD_PAGE);
	}
	
	/**
	 * 新增角色
	 */
	@Before({RoleValidator.class,EvictInterceptor.class})
	@CacheName("/admin/role")
	public void save(){
		id = RoleService.service.save(getModel(Role.class));
		redirect("/admin/role");
	}
	
	/**
	 * 准备更新角色
	 */
	public void edit(){
		Role role=Role.dao.findById(getPara("id"));
		setAttr("role",role);
		render(ROLE_EDIT_PAGE);
	}
	
	/**
	 * 更新角色
	 */
	@Before({RoleValidator.class,EvictInterceptor.class})
	@CacheName("/admin/role")
	public void update(){
		Role role=getModel(Role.class);
		RoleService.service.update(role);
		redirect("/admin/role");
	}
	
	/**
	 * 准备角色分配权限
	 */
	public void assign(){
		Role role=Role.dao.findById(getPara("id"));
		List<String> userpermission=RolePermission.dao.findPermissionIds("role_id=?",role.get("id"));
		String where = "deleted_at is null and pid=?";
		List<Permission> permissions=Permission.dao.findPermissionsByPid(where,0);
		for(Permission permission:permissions){
			List<Permission> pchild=Permission.dao.findPermissionsByPid(where,permission.get("id"));
			permission.setChildren(pchild);
		}
		
		setAttr("role",role);
		setAttr("userpermission",userpermission);
		setAttr("permissions",permissions);
		render(ROLE_ASSIGN_PAGE);
	}
	
	/**
	 * 角色分配权限
	 */
	public void doassign(){
		//页面传值
		String[] permissions=getParaValues("pers");
		Long roleid=getParaToLong("roleid");
		//当前角色已分配权限List
		List<RolePermission> rps=RolePermission.dao.findRolePermissionByRoleId(roleid);
		
		if(ValidateKit.isNullOrEmpty(permissions)){
			for(int i=0;i<rps.size();i++){
				log.error(rps.get(i).getLong("permission_id")+" 已取消，执行删除操作！");
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
	
	/**
	 * 设置不可用
	 */
	@Before({EvictInterceptor.class})
	@CacheName("/admin/role")
	public void off(){
		Role.dao.findById(getPara("id")).set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		redirect("/admin/role");
	}
	
	/**
	 * 设置可用
	 */
	@Before({EvictInterceptor.class})
	@CacheName("/admin/role")
	public void on(){
		Role.dao.findById(getPara("id")).set("deleted_at", null).update();
		redirect("/admin/role");
	}

}
