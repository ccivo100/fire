package com.poicom.function.system.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.dreampie.ValidateKit;
import cn.dreampie.shiro.core.SubjectKit;
import cn.dreampie.sqlinxml.SqlKit;
import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;
/**
 * 
 * @author 唐东宇
 *
 */
@TableBind(tableName="sec_user")
public class User extends Model<User> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1923879818586171796L;
	public static final User dao=new User();
	
	public Page<Record> getUserPage(int pageNumber,int pageSize){
		return Db.paginate(pageNumber, pageSize,
				SqlKit.sql("user.findUserPageBySelect") + blank,
				SqlKit.sql("user.findUserPageByFrom"));
	}
	
	/**
	 * 获取当前登陆用户信息
	 * @return
	 */
	public User getCurrentUser(){
		User user=SubjectKit.getUser();
		if(ValidateKit.isNullOrEmpty(user))
			return null;
		else
			return user;
	}
	
	/**
	 * 根据故障类型id，获得相应运维人员信息
	 * @param paras
	 * @return
	 */
	public List<Record> getOperatorsList(Object... paras){
		return Db.find(
				SqlKit.sql("user.findOperatorsBySelect") + blank
						+ SqlKit.sql("user.findOperatorsByFrom"), paras);
	}
	
	public Page<User> getAllUserPage(int pageNumber,int pageSize,String where,Object... paras){
		return paginate(pageNumber, pageSize,
				SqlKit.sql("user.findInfoBySelect"),
				SqlKit.sql("user.findAllUserByFrom")+getWhere(where), paras);
	}
	
	public User findUserByAttr(String attr,Object paras){
		return findFirst("select * from sec_user where "+attr+" =? ",paras);
	}

}
