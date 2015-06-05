package com.poicom.function.model;

import java.util.List;

import com.jfinal.plugin.ehcache.CacheKit;
import com.poicom.basic.common.DictKeys;
import com.poicom.basic.thread.ThreadParamInit;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.tree.TreeNode;

/**
 * 权限Model
 * @author DONGYU
 *
 */
@TableBind(tableName="sec_permission")
public class Permission extends BaseModel<Permission> implements TreeNode<Permission>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5934957900463679947L;
	public static Permission dao=new Permission();

	@Override
	public long getId() {
		return this.getLong("id");
	}

	@Override
	public long getParentId() {
		return this.getLong("pid");
	}

	@Override
	public List<Permission> getChildren() {
		return this.get("children");
	}

	@Override
	public void setChildren(List<Permission> children) {
		this.put("children", children);
	}
	
	/**
	 * 添加更新缓存
	 * @param id
	 */
	public void cacheAdd(Long id){
		Permission permission = Permission.dao.findById(id);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_permission + id, permission);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_permission + permission.getStr("url"), permission);
	}
	
	/**
	 * 删除缓存
	 * @param id
	 */
	public void cacheRemove(Long id){
		Permission permission = Permission.dao.findById(id);
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_permission + id);
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_permission + permission.getStr("url"));
	}
	
	/**
	 * 获取缓存
	 * @param id
	 * @return
	 */
	public Permission cacheGet(Long id){
		Permission permission = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_permission + id);
		return permission;
	}
	
	/**
	 * 获取指定角色的所有权限
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<Permission> findByRole(String where,Object... paras){
		return find(getSelectSql()+getSql("permission.findRoleByFrom") + blank + getWhere(where), paras);
	}
	
	public List<Permission> findPermissionsByPid(String where,Object... paras){
		return find("select * from sec_permission "+getWhere(where),paras);
	}
	

}
