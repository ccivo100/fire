package com.poicom.function.model;

import java.util.List;

import cn.dreampie.tablebind.TableBind;

/**
 * @描述 用户工单表
 * @author 唐东宇
 *
 */
@TableBind(tableName = "com_user_order")
public class UserOrder extends BaseModel<UserOrder> {

	private static final long serialVersionUID = 4246823198683311680L;
	
	public static final UserOrder dao=new UserOrder();
	
	/**
	 * 根据orderid获取userList
	 * @param paras
	 * @return
	 */
	public List<User> getUserList(Object... paras){
		
		return User.dao.find(getSql("user.userByOrderId"), paras);
	}

}
