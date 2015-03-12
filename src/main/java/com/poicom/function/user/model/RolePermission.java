package com.poicom.function.user.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;

@TableBind(tableName = "sec_role_permission")
public class RolePermission extends Model<RolePermission> {
	public static RolePermission dao=new RolePermission();
	
	/**
	 * 获得Role的所有Permission的Ids
	 * @return
	 */
	public List<String> findPermissionIds(String where, Object... paras){
		return Db.query("SELECT DISTINCT `rolePermission`.permission_id FROM sec_role_permission `rolePermission` " + getWhere(where), paras);
	}
}
