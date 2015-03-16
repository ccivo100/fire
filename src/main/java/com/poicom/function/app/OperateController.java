package com.poicom.function.app;

import java.util.Date;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.poicom.function.app.model.Order;
import com.poicom.function.user.model.User;
import com.poicom.function.user.model.UserInfo;

/**
 * @描述 运维管理
 * @author poicom7
 *
 */
@ControllerKey(value="/operate",path="/page/app/operate")
public class OperateController extends Controller{
	
	public void index(){
		
	}
	
	/**
	 * @描述 运维人员查询本账号处理范围内的故障工单
	 */
	public void deal(){
		User user=User.dao.getCurrentUser();
		setAttr("operatePage",Order.dao.getOperateOrderPage(getParaToInt(0,1), 10, user.get("id")));
		render("operate.html");
	}
	
	/**
	 * @描述 进入故障工单处理页面
	 */
	public void edit(){
		//故障类型
		setAttr("typeList",Order.dao.getAllType());
		
		//工单详细信息
		Record order = Order.dao.getCommonOrder(getParaToInt("id"));
		setAttr("order", order);
		
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		setAttr("userinfo",userinfo);
		
		//获取工单申报者的分公司信息
		Record offer=UserInfo.dao.getUserBranch(order.getLong("offerid"));

		if(!ValidateKit.isNullOrEmpty(offer))
			setAttr("offer_branch",offer.getStr("branch"));

		
	}
	
	/**
	 * @描述 提交故障处理建议
	 */
	@Before(Tx.class)
	public void update(){
		
		//order_id
		Integer orderid=getParaToInt("orderid");
		//description
		String comment=getPara("commen");
		
		Order.dao.findById(orderid).set("deal_user", getParaToInt("dealid"))
				.set("comment", comment).set("deal_at", new Date())
				.set("status", 1).update();
		
		redirect("/operate/deal");
	}
	
	/**
	 * @描述 发送邮件通知
	 */
	public void sendEmail(){
		
	}
	

}
