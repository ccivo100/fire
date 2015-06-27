package com.poicom.function.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.poicom.basic.common.DictKeys;
import com.poicom.basic.kit.ValiKit;
import com.poicom.basic.plugin.sqlxml.SqlXmlKit;
import com.poicom.basic.thread.ThreadParamInit;

import cn.dreampie.ValidateKit;
import cn.dreampie.shiro.core.SubjectKit;
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
	
	/**
	 * 关联查询，获取用户详细信息
	 * @return
	 */
	public UserInfo getUserInfo(){
		Long userid = getLong("id");
		UserInfo userinfo = UserInfo.dao.findFirstBy("user_id=?", userid);
		if(ValiKit.isNullOrEmpty(userinfo)){
			return null;
		}
		return userinfo;
	}
	
	/**
	 * 关联查询，获取用户分公司信息
	 * @return
	 */
	public Branch getBranch(){
		Long branchid = getUserInfo().getLong("branch_id");
		Branch branch = Branch.dao.findById(branchid);
		if(ValiKit.isNullOrEmpty(branch)){
			return null;
		}
		return branch;
	}
	
	/**
	 * 关联查询，获取用户部门信息
	 * @return
	 */
	public Apartment getDepartment(){
		Long departmentid = getUserInfo().getLong("apartment_id");
		Apartment department = Apartment.dao.findById(departmentid);
		if(ValiKit.isNullOrEmpty(department)){
			return null;
		}
		return department;
	}
	
	/**
	 * 关联查询，获取用户职位信息
	 * @return
	 */
	public Position getPosition(){
		Long positionid = getUserInfo().getLong("position_id");
		Position position = Position.dao.findById(positionid);
		if(ValiKit.isNullOrEmpty(position)){
			return null;
		}
		return position;
	}
	
	/**
	 * 添加或更新缓存
	 * @param id
	 */
	public void cacheAdd(Long id){
		User user = User.dao.findById(id);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + id, user);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("username"), user);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("email"), user);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("phone"), user);
	}
	
	/**
	 * 删除缓存
	 * @param id
	 */
	public void cacheRemove(Long id){
		User user = User.dao.findById(id);
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + id);
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("username"));
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("email"));
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("phone"));
	}
	
	
	/**
	 * 获取缓存
	 * @param id
	 * @return
	 */
	public User cacheGet(Long id){
		User user = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + id);
		return user;
	}
	
	
	public Page<Record> getUserPage(int pageNumber,int pageSize){
		return Db.paginate(pageNumber, pageSize,
				SqlXmlKit.sql("user.findUserPageBySelect") + blank,
				SqlXmlKit.sql("user.findUserPageByFrom"));
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
				SqlXmlKit.sql("user.findOperatorsBySelect") + blank
						+ SqlXmlKit.sql("user.findOperatorsByFrom"), paras);
	}
	
	public Page<User> getAllUserPage(int pageNumber,int pageSize,String where,Object... paras){
		return paginate(pageNumber, pageSize,
				SqlXmlKit.sql("user.findInfoBySelect"),
				SqlXmlKit.sql("user.findAllUserByFrom")+getWhere(where), paras);
	}
	
	/*public User findUserByAttr(String attr,Object paras){
		return findFirst("select * from sec_user where "+attr+" =? ",paras);
	}*/

}
