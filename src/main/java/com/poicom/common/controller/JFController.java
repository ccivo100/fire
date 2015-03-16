package com.poicom.common.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.poicom.function.app.model.Order;
import com.poicom.function.user.model.User;
import com.poicom.function.user.model.UserInfo;

public class JFController extends Controller{
	
	/**
	 * 报障人员、运维人员，查询故障工单详细信息
	 */
	protected void query(){
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		setAttr("typeList",Order.dao.getAllType());
		
		setAttr("userinfo",userinfo);
		setAttr("order",Order.dao.getCommonOrder(getParaToInt("id")));
		render("/page/app/common/query.html");
	}

}
