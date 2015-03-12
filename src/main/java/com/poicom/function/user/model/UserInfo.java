package com.poicom.function.user.model;

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
}
