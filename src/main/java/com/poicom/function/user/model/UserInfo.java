package com.poicom.function.user.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
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
	
	public static UserInfo dao=new UserInfo();
	
	/**
	 * 获得当前用户的所有信息
	 * @param paras
	 * @return
	 */
	public Record getAllUserInfo(Object... paras){
		return Db.findFirst(SqlKit.sql("user.findAllBySelect")+blank+SqlKit.sql("user.findAllByFrom"),paras);
	}
	
}
