package com.poicom.function.user.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.dreampie.sqlinxml.SqlKit;
import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;

/**
 * 用户信息表
 * @author DONGYU
 *
 */
@TableBind(tableName = "sec_user_info")
public class UserInfo extends Model<UserInfo> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3424907544751263092L;
	public static UserInfo dao=new UserInfo();
	
	/**
	 * 根据用户id，获得该用户的所有信息
	 * @param paras
	 * @return
	 */
	public Record getAllUserInfo(Object... paras){
		return Db.findFirst(SqlKit.sql("user.findInfoBySelect") + blank
				+ SqlKit.sql("user.findInfoByFrom"), paras);
	}
	
	/**
	 * 根据用户id，获得该用户的分公司信息
	 * @param paras
	 * @return
	 */
	public Record getUserBranch(Object... paras){
		return Db.findFirst(SqlKit.sql("user.findUserBranchBySelect") + blank
				+ SqlKit.sql("user.findUserBranchByFrom"), paras);
	}
	
	/**
	 * @描述 根据用户Branch_id，查找该部门用户详细信息
	 * @param pageNumber
	 * @param pageSize
	 * @param paras
	 * @return
	 */
	public Page<Record> getUserByBranch(int pageNumber,int pageSize,Object... paras){
		return Db.paginate(pageNumber, pageSize,
				SqlKit.sql("user.findBranchUserBySelect") + blank,
				SqlKit.sql("user.findBranchUserByFrom"), 10);
	}
	
	public UserInfo get(String attr,Object paras){
		return findFirst("select * from sec_user_info where "+attr+"=?", paras);
	}
	
}
