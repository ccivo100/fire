package com.poicom.function.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.ehcache.CacheKit;
import com.poicom.basic.common.DictKeys;
import com.poicom.basic.thread.ThreadParamInit;

import cn.dreampie.ValidateKit;
import cn.dreampie.tablebind.TableBind;
import cn.dreampie.tree.TreeNode;
/**
 * 角色Model
 * @author 唐东宇
 *
 */
@TableBind(tableName="sec_role")
public class Role extends BaseModel<Role> implements TreeNode<Role>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -588034349380836740L;
	public static final Role dao=new Role();

	@Override
	public long getId() {
		return this.getLong("id");
	}

	@Override
	public long getParentId() {
		return this.getLong("pid");
	}

	@Override
	public List<Role> getChildren() {
		return this.get("children");
	}

	@Override
	public void setChildren(List<Role> children) {
		this.put("children", children);
	}
	/**
	 * 添加或更新缓存
	 * @param id
	 */
	public void cacheAdd(Long id){
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_role + id, Role.dao.findById(id));
	}
	
	/**
	 * 删除缓存
	 * @param id
	 */
	public void cacheRemove(Long id){
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_role + id);
	}
	
	/**
	 * 获取缓存
	 * @param id
	 * @return
	 */
	public Role cacheGet(Long id){
		Role role = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_role + id);
		return role;
	}
	
	/**
	 * 根据role的id，获得相应角色的权限；
	 * 如role：超级管理员；
	 * 则permission：all。
	 */
	public void initPermissiones(){
		this.put("permissions", Permission.dao.findByRole("", this.getInt("id")));
	}
	
	public List<Permission> getPermissiones(){
		return this.get("permissiones");
	}
	
	/**
	 * 为Role添加权限Permission
	 * @param permission
	 * @return role
	 */
	public Role addPermission(Permission permission){
		if(ValidateKit.isNullOrEmpty(permission)){
			throw new NullPointerException("操作权限不存在！");
		}
		RolePermission rolePermission=new RolePermission();
		rolePermission.set("role_id", this.get("id"));
		rolePermission.set("permission_id", permission.get("id"));
		return this;
	}
	
	/**
	 * 根据User的id，获得该User的所有角色；
	 * 因为Role和User也是多对多关系。
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<Role> findUserBy(String where, Object... paras){
		return find(getSelectSql() + getSql("role.findUserByFrom") + blank + getWhere(where), paras);
	}
	
	/**
	 * 获得Role的children
	 * 配置文件role.findChildrenByFrom有问题？
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<Role> findChildrenById(String where,Object... paras){
		return find(getSelectSql() + getSql("role.findChildrenByFrom") + blank + getWhere(where), paras);
	}
	
	/**
	 * 获得Role的children的Ids
	 * 配置文件role.findChildrenByFrom有问题？
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<Long> findChildrenIdsById(String where,Object... paras){
		return Db.query("SELECT `role`.id " + getSql("role.findChildrenByFrom") + blank + getWhere(where), paras);
	}
	
	/**
	 * @描述 获得根角色List<Role>
	 * @return
	 */
	public List<Role> findRolesByPid(Object... paras){
		return find("select * from sec_role where pid=?",paras);
	}
	
	/**
	 * 根据参数个数组装sql语句。
	 * @param paras
	 * @return
	 */
	public List<Role> findRolesExceptId(Object... paras){
		return find("select * from sec_role where id not in"+getParaNumber(paras),paras);
	}
	


}
