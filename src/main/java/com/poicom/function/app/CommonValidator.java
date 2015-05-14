package com.poicom.function.app;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;
import cn.dreampie.shiro.core.SubjectKit;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.validate.Validator;
import com.poicom.function.app.model.ErrorType;
import com.poicom.function.app.model.Level;
import com.poicom.function.app.model.Order;
import com.poicom.function.system.model.User;
import com.poicom.function.system.model.UserInfo;
/**
 * 
 * @author 唐东宇
 *
 */
public class CommonValidator extends Validator{
	
	protected Logger logger=LoggerFactory.getLogger(getClass());

	@Override
	protected void validate(Controller c) {
		
		if(getActionKey().equals("/report/save")|getActionKey().equals("/report/update")){
			if(ValidateKit.isNullOrEmpty(c.getPara("otitle"))){
				addError("descriptionMsg","故障单标题不能为空！");
			}else if(ValidateKit.isNullOrEmpty(c.getPara("odescription")))
			{
				addError("descriptionMsg","故障单描述不能为空！");
			} 
		}
		else if(getActionKey().equals("/report/edit")){
			int orderid=c.getParaToInt("id");
			Order order=Order.dao.findById(orderid);
			if(ValidateKit.isNullOrEmpty(order)){
				addError("errorMsg","失败：无法查询到该工单。");
			}
			else if(order.getInt("status")==1){
				addError("errorMsg","失败：工单正在处理，无法修改。");
			}else if(order.getInt("status")==0){
				addError("errorMsg","失败：工单已处理，无法修改。");
			}else if(!ValidateKit.isNullOrEmpty(order.get("deleted_at"))){
				addError("errorMsg","失败：工单已撤销，无法修改。");
			}
		}
		else if(getActionKey().equals("/operate/update")){
			int orderid=c.getParaToInt("oorderid");
			Order order=Order.dao.findById(orderid);
			User cUser=SubjectKit.getUser();
			if(!ValidateKit.isLength(c.getPara("ocomment"), 2, 250))
			{
				addError("commentMsg","处理意见应不少于2字！");
			}else if(!ValidateKit.isNullOrEmpty(order.getLong("deal_user"))){
				if(cUser.getLong("id").equals(order.getLong("accept_user"))){
					addError("commentMsg","失败：工单已指派其他运维员处理");
				}
			}
		}else if(getActionKey().equals("/operate/edit")){
			int orderid=c.getParaToInt("id");
			Order order=Order.dao.findById(orderid);
			User cUser=SubjectKit.getUser();
			if(ValidateKit.isNullOrEmpty(order)){
				addError("errorMsg","失败：无法查询到该工单。");
			}
			else if(order.getInt("status")==0){
				addError("errorMsg","失败：工单已提交");
			}else if(!ValidateKit.isNullOrEmpty(order.get("deleted_at"))){
				addError("errorMsg","失败：工单已被撤销");
			}
			/*if(order.getInt("status")==0){
				if(!ValidateKit.isNullOrEmpty(order.get("addcomment"))){
					addError("errorMsg","失败：工单已提交");
				}
			}else if(!ValidateKit.isNullOrEmpty(order.getLong("deal_user"))){
				if(cUser.getLong("id").equals(order.getLong("accept_user"))){
					addError("errorMsg","失败：工单已指派其他运维员处理");
				}else if(!cUser.getLong("id").equals(order.getLong("deal_user"))){
					addError("errorMsg","失败：工单非当前用户所属");
				}
				
			}else if(ValidateKit.isNullOrEmpty(order.getLong("deal_user"))){
				if(!cUser.getLong("id").equals(order.getLong("accept_user"))){
					addError("errorMsg","失败：工单非当前用户所属");
				}
			}*/
		}
		
	}

	@Override
	protected void handleError(Controller c) {
		
		//新增工单验证
		if(getActionKey().equals("/report/save")){
			c.keepPara();
			c.setAttr("typeList",ErrorType.dao.getAllType());
			c.setAttr("levelList",Level.dao.findAll());
			c.keepPara("uuserid");
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("oorderid"))){
				c.keepPara("oorderid");
			}
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("otypeid"))){
				c.setAttr("otypeid", null);
			}
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("olevelid"))){
				c.setAttr("olevelid", null);
			}
			
			c.render("/page/app/report/add.html");
		}
		//修改工单验证
		if(getActionKey().equals("/report/update")){
			c.keepPara();
			c.setAttr("typeList",ErrorType.dao.getAllType());
			c.setAttr("levelList",Level.dao.findAll());
			c.keepPara("uuserid");
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("oorderid"))){
				c.keepPara("oorderid");
			}
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("otypeid"))){
				c.keepPara("otypeid");
			}
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("olevelid"))){
				c.keepPara("olevelid");
			}
			
			c.render("/page/app/report/edit.html");
		}
		if(getActionKey().equals("/report/edit")){
			c.render("/app/common/errorMsg.html");
		}
		//处理工单验证
		if(getActionKey().equals("/operate/update")){
			c.keepPara();
			c.setAttr("typeList",ErrorType.dao.getAllType());
			c.setAttr("levelList",Level.dao.findAll());
			c.keepPara("uuserid");
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("oorderid"))){
				c.keepPara("oorderid");
			}
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("otypeid"))){
				int otypeid=c.getParaToInt("otypeid");
				c.setAttr("otypeid", otypeid);
			}
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("olevelid"))){
				int olevelid=c.getParaToInt("olevelid");
				c.setAttr("olevelid", olevelid);
			}
			
			/*if(!ValidateKit.isNullOrEmpty(c.getPara("ostatus"))){
				if(c.getPara("ostatus").equals("已处理")){
					c.setAttr("ostatus", 0);
				}else if(c.getPara("ostatus").equals("未处理")){
					c.setAttr("ostatus", 1);
				}else if(c.getPara("ostatus").equals("超时未处理")){
					c.setAttr("ostatus", 2);
				}
			}*/
			String where=" userinfo.apartment_id=? and user.id<>?";
			List<Record> dealList=UserInfo.dao.getUserByApartment(where,2,User.dao.getCurrentUser().get("id"));
			c.setAttr("dealList",dealList);
			
			c.render("/page/app/operate/edit.html");
		}
		if(getActionKey().equals("/operate/edit")){
			c.render("/app/common/errorMsg.html");
		}
		
		
	}

}
