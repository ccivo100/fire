package com.poicom.function.user.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.dreampie.sqlinxml.SqlKit;
import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;

@TableBind(tableName="sec_user")
public class User extends Model<User> {
	
	public static User dao=new User();
	
	public Page<Record> getUserPage(int pageNumber,int pageSize){
		return Db.paginate(pageNumber, pageSize, SqlKit.sql("user.findUserPageBySelect")+ blank, SqlKit.sql("user.findUserPageByFrom"));
	}
	

}
