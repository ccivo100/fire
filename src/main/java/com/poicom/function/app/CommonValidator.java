package com.poicom.function.app;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.app.model.ErrorType;

public class CommonValidator extends Validator{
	
	protected Logger logger=LoggerFactory.getLogger(getClass());

	@Override
	protected void validate(Controller c) {
		
		if(getActionKey().equals("/report/save")|getActionKey().equals("/report/update")){
			
			if(!ValidateKit.isLength(c.getPara("odescription"), 2, 250))
			{
				addError("descriptionMsg","故障描述应不少于2字！");
			}
		}
		else if(getActionKey().equals("/operate/update")){
			if(!ValidateKit.isLength(c.getPara("ocomment"), 2, 250))
			{
				addError("commentMsg","处理意见应不少于2字！");
			}
		}
		
	}

	@Override
	protected void handleError(Controller c) {
		
		//新增工单验证
		if(getActionKey().equals("/report/save")){
			c.keepPara();
			c.setAttr("typeList",ErrorType.dao.getAllType());
			c.keepPara("uuserid");
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("oorderid"))){
				c.keepPara("oorderid");
			}
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("otypeid"))){
				c.setAttr("otypeid", null);
			}
			
			c.render("/page/app/report/add.html");
		}
		//修改工单验证
		if(getActionKey().equals("/report/update")){
			c.keepPara();
			c.setAttr("typeList",ErrorType.dao.getAllType());
			c.keepPara("uuserid");
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("oorderid"))){
				c.keepPara("oorderid");
			}
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("otypeid"))){
				c.keepPara("otypeid");
			}
			
			c.render("/page/app/report/edit.html");
		}
		//处理工单验证
		if(getActionKey().equals("/operate/update")){
			c.keepPara();
			c.setAttr("typeList",ErrorType.dao.getAllType());
			c.keepPara("uuserid");
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("oorderid"))){
				c.keepPara("oorderid");
			}
			if(!ValidateKit.isNullOrEmpty(c.getParaToInt("otypeid"))){
				int otypeid=c.getParaToInt("otypeid");
				c.setAttr("otypeid", otypeid);
			}
			
			if(!ValidateKit.isNullOrEmpty(c.getPara("ostatus"))){
				if(c.getPara("ostatus").equals("已处理")){
					c.setAttr("ostatus", 0);
				}else if(c.getPara("ostatus").equals("未处理")){
					c.setAttr("ostatus", 1);
				}else if(c.getPara("ostatus").equals("超时未处理")){
					c.setAttr("ostatus", 2);
				}
			}
			
			c.render("/page/app/operate/edit.html");
		}
		
		
	}

}
