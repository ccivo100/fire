package com.poicom.function.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;

import cn.dreampie.tablebind.TableBind;
/**
 * 
 * @author 陈宇佳
 *
 */
@TableBind(tableName = "sec_role_permission")
public class RolePermission extends BaseModel<RolePermission> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1485058649065474980L;
	public static RolePermission dao=new RolePermission();
	
	/**
	 * 获得Role的所有Permission的Ids
	 * @return
	 */
	public List<String> findPermissionIds(String where, Object... paras){
		return Db.query("SELECT DISTINCT `rolePermission`.permission_id FROM sec_role_permission `rolePermission` " + getWhere(where), paras);
	}
	
	public List<RolePermission> findRolePermissionByRoleId(Object... paras){
		return find("select * from sec_role_permission where role_id=?",paras);
	}
}
