package com.poicom.function.app.model;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;

/**
 * @描述 用户工单表
 * @author 唐东宇
 *
 */
@TableBind(tableName = "com_user_order")
public class UserOrder extends Model<UserOrder> {

	private static final long serialVersionUID = 4246823198683311680L;
	
	public static final UserOrder dao=new UserOrder();
	

}
