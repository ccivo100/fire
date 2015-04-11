package com.poicom.function.system.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;

import cn.dreampie.ValidateKit;
import cn.dreampie.sqlinxml.SqlKit;
import cn.dreampie.tablebind.TableBind;
import cn.dreampie.tree.TreeNode;
import cn.dreampie.web.model.Model;

@TableBind(tableName="sec_role")
public class Role extends Model<Role> implements TreeNode<Role>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -588034349380836740L;
	public static Role dao=new Role();

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
		return find(getSelectSql() + SqlKit.sql("role.findUserByFrom") + blank + getWhere(where), paras);
	}
	
	/**
	 * 获得Role的children
	 * 配置文件role.findChildrenByFrom有问题？
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<Role> findChildrenById(String where,Object... paras){
		return find(getSelectSql() + SqlKit.sql("role.findChildrenByFrom") + blank + getWhere(where), paras);
	}
	
	/**
	 * 获得Role的children的Ids
	 * 配置文件role.findChildrenByFrom有问题？
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<Long> findChildrenIdsById(String where,Object... paras){
		return Db.query("SELECT `role`.id " + SqlKit.sql("role.findChildrenByFrom") + blank + getWhere(where), paras);
	}
	
	/**
	 * @描述 获得根角色List<Role>
	 * @return
	 */
	public List<Role> findRolesByPid(Object... paras){
		return find("select * from sec_role where pid=?",paras);
	}
	

}
