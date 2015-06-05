package com.poicom.function.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;
import com.poicom.function.model.Permission;
import com.poicom.function.service.PermissionService;
import com.poicom.function.validator.PermissionValidator;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;


@ControllerKey(value = "/admin/permission", path="/app/admin")
public class PermissionController extends BaseController {
	
	private static Logger log = Logger.getLogger(PermissionController.class);
	
	private final static String PERMISSION_ADD_PAGE="permission/add.html";
	private final static String PERMISSION_EDIT_PAGE="permission/edit.html";
	
	/**
	 * 权限列表
	 */
	@Before(CacheInterceptor.class)
	@CacheName("/admin/permission")
	public void index(){
		String where = " pid=?";
		List<Permission> permissions=Permission.dao.findPermissionsByPid(where,0);
		for(Permission permission:permissions){
			List<Permission> pchild=Permission.dao.findPermissionsByPid(where,permission.get("id"));
			permission.setChildren(pchild);
		}
		setAttr("permissionTree",permissions);
		render("permission.html");
	}
	
	/**
	 * 准备新增权限
	 */
	public void add(){
		if(ValidateKit.isNullOrEmpty(getPara("id"))){

		}else{
			System.out.println(getParaToLong("id"));
			setAttr("pid",getParaToLong("id"));
		}
		render(PERMISSION_ADD_PAGE);
	}
	
	/**
	 * 新增权限
	 */
	@Before({PermissionValidator.class,EvictInterceptor.class})
	@CacheName("/admin/permission")
	public void save(){
		id = PermissionService.service.save(getModel(Permission.class));
		redirect("/admin/permission");
	}
	
	/**
	 * 准备更新权限
	 */
	public void edit(){
		Permission permission=Permission.dao.findById(getPara("id"));
		setAttr("permission",permission);
		render(PERMISSION_EDIT_PAGE);
	}
	
	/**
	 * 更新权限
	 */
	@Before({PermissionValidator.class,EvictInterceptor.class})
	@CacheName("/admin/permission")
	public void update(){
		Permission permission=getModel(Permission.class);
		PermissionService.service.update(permission);
		redirect("/admin/permission");
	}
	
	/**
	 * 设置不可用
	 */
	@Before({EvictInterceptor.class})
	@CacheName("/admin/permission")
	public void off(){
		PermissionService.service.off(Permission.dao.findById(getPara("id")));
		redirect("/admin/permission");
	}
	
	/**
	 * 设置可用
	 */
	@Before({EvictInterceptor.class})
	@CacheName("/admin/permission")
	public void on(){
		
		PermissionService.service.on(Permission.dao.findById(getPara("id")));
		redirect("/admin/permission");
	}
	

}
