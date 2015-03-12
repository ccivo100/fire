package com.poicom.function.user.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;

import cn.dreampie.sqlinxml.SqlKit;
import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;

@TableBind(tableName = "sec_user_role")
public class UserRole extends Model<UserRole> {
	public static UserRole dao=new UserRole();
	
	/**
	 * this.get("role_id")为UserRole根据role_id获得一个Obj
	 * @return
	 */
	public Role getRole(){
		return Role.dao.findById(this.get("role_id"));
	}
	
	/**
	 * 根据条件，获得角色Role的所有User
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<String> findUserIds(String where,Object... paras){
		return Db.query("SELECT DISTINCT `userRole`.user_id " + SqlKit.sql("userRole.findByFrom") + " " + getWhere(where), paras);
	}
}
