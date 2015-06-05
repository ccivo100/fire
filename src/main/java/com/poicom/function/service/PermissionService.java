package com.poicom.function.service;

import org.joda.time.DateTime;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.WebKit;
import com.poicom.function.model.Permission;

public class PermissionService extends BaseService {
	
	private static Logger log = Logger.getLogger(PermissionService.class);
	
	public static final PermissionService service = new PermissionService();
	
	/**
	 * 保存
	 * @param permission
	 * @return
	 */
	public Long save(Permission permission){
		permission.set("name", WebKit.delHTMLTag(permission.getStr("name"))).set("value", WebKit.delHTMLTag(permission.getStr("value"))).set("url", WebKit.delHTMLTag(permission.getStr("url")));
		permission.save();
		return permission.getPKValue();
	}
	
	/**
	 * 更新、设置不可用
	 * @param permission
	 */
	public void update(Permission permission){
		permission.set("name", WebKit.delHTMLTag(permission.getStr("name"))).set("value", WebKit.delHTMLTag(permission.getStr("value"))).set("url", WebKit.delHTMLTag(permission.getStr("url")));
		permission.update();
		Permission.dao.cacheAdd(permission.getPKValue());
	}
	
	/**
	 * 删除
	 * @param permissionid
	 */
	public void drop(Long permissionid){
		Permission.dao.cacheRemove(permissionid); 
		Permission.dao.deleteById(permissionid);
	}
	
	public boolean off(Permission permission){
		return permission.set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		
	}
	
	public boolean on(Permission permission){
		return permission.set("deleted_at", null).update();
	}
	
	

}
