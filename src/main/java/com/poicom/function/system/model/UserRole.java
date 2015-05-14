package com.poicom.function.system.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.dreampie.sqlinxml.SqlKit;
import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;
/**
 * 
 * @author 唐东宇
 *
 */
@TableBind(tableName = "sec_user_role")
public class UserRole extends Model<UserRole> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1249545626388057330L;
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
	
	/**
	 * @描述 根据用户id，查询其相应角色
	 * @param paras
	 * @return
	 */
	public List<Record> findUserRolesById(Object... paras){
		return Db.find(SqlKit.sql("userRole.findUserRolesBySelect") + blank
				+ SqlKit.sql("userRole.findUserRolesByFrom"), paras);
	}
}
