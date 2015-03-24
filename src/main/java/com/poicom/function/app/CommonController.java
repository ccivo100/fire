package com.poicom.function.app;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import com.poicom.common.controller.JFController;
import com.poicom.function.app.model.Order;
import com.poicom.function.user.model.UserInfo;

/**
 * 
 * @author poicom7
 *
 */
@ControllerKey(value = "/order", path = "/page/app/common")
public class CommonController extends JFController{
	
	/**
	 * 查询故障工单详细内容
	 */
	@Before(CacheInterceptor.class)
	@CacheName("/order/query")
	public void query(){
		Record order = Order.dao.getCommonOrder(getParaToInt("id"));

		//获取工单申报者的分公司信息
		Record offer=UserInfo.dao.getUserBranch(order.getLong("oofferid"));
		//获取工单处理者的分公司信息
		Record deal=UserInfo.dao.getUserBranch(order.getLong("odealid"));
		
		if(!ValidateKit.isNullOrEmpty(offer))
			setAttr("offer_branch",offer.getStr("bname"));
		if(!ValidateKit.isNullOrEmpty(deal))
			setAttr("deal_branch",deal.getStr("bname"));
		//工单详细信息
		setAttr(order);
		setAttr("order", order);
		//故障类型
		setAttr("typeList",Order.dao.getAllType());
		
		render("/page/app/common/query.html");
	}
	

}
