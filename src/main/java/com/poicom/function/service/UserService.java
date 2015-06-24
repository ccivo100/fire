package com.poicom.function.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;
import com.poicom.basic.common.DictKeys;
import com.poicom.basic.common.SplitPage;
import com.poicom.function.model.Apartment;
import com.poicom.function.model.User;
import com.poicom.function.model.UserInfo;

/**
 * 用戶管理业务层
 * @author FireTercel 2015年6月4日 
 *
 */
public class UserService extends BaseService {
	
	private static Logger log = Logger.getLogger(UserService.class);
	
	public static final UserService service = new UserService();
	
	/**
	 * 保存
	 * @param user
	 * @param password
	 * @param userInfo
	 */
	public void save(User user,String password,UserInfo userInfo){
		
		user.save();
		userInfo.save();
		
		User.dao.cacheAdd(Long.valueOf(user.getPrimaryKey()));
	}
	
	/**
	 * 分页
	 * @param splitPage
	 */
	public void list(SplitPage splitPage){
		String select = "select user.id, user.username, user.email, user.full_name, branch.name, apartment.name, position.name ";
		splitPageBase(DictKeys.db_dataSource_main, splitPage, select, "user.splitPage");
	}
	
	/**
	 * 查询用户所在大部门的所有人员
	 */
	public List<User> usersByApartment(User user){
		List<User> userList =new ArrayList<User>();
		//user所属部门
		Apartment apartment = user.getDepartment();
		
		//user部门同级部门
		List<Apartment> apartmentlist = Apartment.dao.rootNode(apartment.get("pid"));
		
		for(Apartment apt : apartmentlist){
			List<User> uList = Apartment.dao.getUsersById(apt.getPKValue());
			userList.addAll(uList);
		}
		return userList;
	}
	
	/**
	 * 查询用户所在大部门的所有人员详细信息
	 * @param user
	 * @return
	 */
	public List<Record> userinfosByApartment(User user){
		List<Record> userinfoList = new ArrayList<Record>();
		//user所属部门
		Apartment apartment = user.getDepartment();
		//user部门同级部门
		List<Apartment> apartmentlist = Apartment.dao.rootNode(apartment.get("pid"));
		for(Apartment apt : apartmentlist){
			List<Record> uList = UserInfo.dao.getUserByApartment(" apartment.id=? and user.deleted_at is null",apt.getPKValue());
			userinfoList.addAll(uList);
		}
		return userinfoList;
	}

}
