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
 * @描述 故障申报
 * @author poicom7
 *
 */
@ControllerKey(value = "/report", path = "/page/app/report")
public class ReportController extends Controller{
	
	public void index(){
		
		render("order");
	}

	
	
	/**
	 * @描述 报障人员查询本账号申报的故障工单
	 */
	public void offer(){
		User user=User.dao.getCurrentUser();
		System.out.println(user.get("id"));
		setAttr("reportPage",Order.dao.getReportOrderPage(getParaToInt(0,1), 10,user.get("id")));
		render("report.html");
	}
	
	
	//测试
	public void print(){
		System.out.println("id="+getPara("id"));
		redirect("/report/offer");
	}
	
	/**
	 * @描述 进入新建故障工单页面
	 */
	public void add(){
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		setAttr("userinfo",userinfo);
		setAttr("typeList",Order.dao.getAllType());
		render("add.html");
	}
	
	/**
	 * @描述 新建故障工单
	 */
	public void save(){
		//offer_user
		Integer userid=getParaToInt("userid");
		//type
		Integer selectType=getParaToInt("selectType");
		//description
		String description=getPara("description");
		
		Order order=new Order().set("offer_user", getParaToInt("userid"))
				.set("description", getPara("description"))
				.set("type", getParaToInt("selectType"))
				.set("status", 0).set("offer_at", new Date());
		if(order.save())
			redirect("/report/offer");
		else
			System.out.println("userid:"+userid+" type:"+selectType+" des:"+description);
		
		//发送邮件通知
		sendEmail();
		
		redirect("/report/offer");
	}
	
	/**
	 * @描述 申报人员可以修改未被处理的工单
	 *            此时不会再次发送邮件、短信通知。
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
	 *  @描述 处理申报人员更新故障工单操作 
	 */
	@Before(Tx.class)
	public void update(){
		//order_id
		Integer orderid=getParaToInt("orderid");
		//description
		String description=getPara("description");
		
		Order.dao.findById(orderid).set("description", getPara("description")).set("updated_at", new Date()).update();
		
		redirect("/report/offer");
		
	}
	
	/**
	 * @描述 发送邮件通知
	 */
	public void sendEmail(){
		
	}

}
