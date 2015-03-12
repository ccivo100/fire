package com.poicom.function.app;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;
import cn.dreampie.shiro.core.SubjectKit;

import com.jfinal.core.Controller;
import com.poicom.function.app.model.Order;
import com.poicom.function.user.model.User;

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
	 * 获取当前登陆用户信息
	 * @return
	 */
	private User getCurrentUser(){
		User user=SubjectKit.getUser();
		if(ValidateKit.isNullOrEmpty(user))
			return null;
		else
			return user;
	}
	
	/**
	 * @描述 报障人员查询本账号申报的工单
	 */
	public void offer(){
		User user=getCurrentUser();
		System.out.println(user.get("id"));
		setAttr("reportPage",Order.dao.getReportOrderPage(getParaToInt(0,1), 10,user.get("id")));
		render("report.html");
	}
	
	/**
	 * 查询工单详细内容
	 */
	public void query(){
		
	}
	
	public void print(){
		System.out.println("id="+getPara("id"));
		redirect("/report/offer");
	}
	
	/**
	 * 新建工单
	 */
	public void save(){
		
	}
	
	/**
	 * 发送邮件通知
	 */
	public void sendEmail(){
		
	}

}
