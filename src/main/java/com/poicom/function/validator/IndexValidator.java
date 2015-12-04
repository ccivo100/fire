package com.poicom.function.validator;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.validate.Validator;
import com.poicom.basic.kit.DateKit;
import com.poicom.function.model.Retrieve;
import com.poicom.function.model.User;
/**
 * 
 * @author 陈宇佳
 *
 */
public class IndexValidator extends Validator{
	
	protected Logger logger=LoggerFactory.getLogger(getClass());
	private final static String MSG_SEARCH="searchMsg";
	private final static String MSG_RETRIEVE="retrieveMsg";

	/**
	 * 验证查询条件
	 * 验证找回密码用户名 和 邮箱 匹配情况
	 */
	@Override
	protected void validate(Controller c) {
		
		if(getActionKey().equals("/")){
			boolean isLength=ValidateKit.isLength(c.getPara("condition"), 0, 10);
			if(!isLength){
				addError("searchMsg","查询字数不可超过10，请重新输入");
			}
		}else if(getActionKey().equals("/doretrieve")){
			String username=c.getPara("username");
			String email=c.getPara("email");
			if(ValidateKit.isNullOrEmpty(username)){
				addError("retrieveMsg","用户名不能为空");
				
			}else if(!ValidateKit.isNullOrEmpty(username)){
				User user=User.dao.findFirstBy("`user`.username = ? AND `user`.deleted_at is null", username);
				if(ValidateKit.isNullOrEmpty(user)){
					addError("retrieveMsg","查无此账号");
				}else if(ValidateKit.isNullOrEmpty(email)){
					addError("retrieveMsg","邮箱地址不能为空");
				}else if(!ValidateKit.isEmail(email)){
					addError("retrieveMsg","邮箱地址格式不正确");
				}else if(!user.get("email").equals(email)){
					addError("retrieveMsg","用户名与邮箱地址不匹配");
				}
			}
			
			
		}else if(getActionKey().equals("/dorepassword")){
			String newValidCode=c.getPara("newValidCode");
			String email=c.getPara("email");
			String password=c.getPara("password");
			String repassword=c.getPara("repassword");
			boolean passwordEmpty=ValidateKit.isNullOrEmpty(password);
			boolean repasswordEmpty=ValidateKit.isNullOrEmpty(repassword);
			
			if(!ValidateKit.isEmail(email)||ValidateKit.isNullOrEmpty(newValidCode)){
				addError("passwordMsg","数据异常");
			}else{
				Retrieve retrieve=Retrieve.dao.findFirstBy("`retrieve`.email = ? ORDER BY `retrieve`.created_at DESC", email);
				if(ValidateKit.isNullOrEmpty(retrieve)){
					addError("passwordMsg","无效请求参数，密码重置请点击");
				}else if(DateKit.dateBetween(retrieve.get("created_at").toString(), DateTime.now())>PropKit.getInt("retrieve.time", 24)){
					addError("passwordMsg","链接已过期，密码重置请点击");
				}else if(!newValidCode.equals(retrieve.get("random"))){
					addError("passwordMsg","无效请求参数，密码重置请点击");
				}else if(!passwordEmpty&&!ValidateKit.isPassword(repassword)){
					addError("passwordMsg","新密码为英文字母 、数字和下划线长度为5-18");
				}else if(!passwordEmpty&&!password.equals(repassword)){
					addError("passwordMsg","重复密码不匹配");
				}
			}
			
			
		}
		
	}

	@Override
	protected void handleError(Controller c) {
		if(getActionKey().equals("/")){
			c.keepPara("condition");
			c.render("index.html");
		}else if(getActionKey().equals("/doretrieve")){
			c.keepPara("username");
			c.keepPara("email");
			c.render("retrieve.html");
		}else if(getActionKey().equals("/dorepassword")){
			
			c.render("repassword.html");
		}
		
	}
	
	

}
