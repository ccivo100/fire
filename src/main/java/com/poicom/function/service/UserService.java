package com.poicom.function.service;

import com.jfinal.log.Logger;
import com.poicom.basic.common.DictKeys;
import com.poicom.basic.common.SplitPage;
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

}
