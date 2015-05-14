package com.poicom.function.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;
import cn.dreampie.shiro.core.SubjectKit;
import cn.dreampie.shiro.hasher.Hasher;
import cn.dreampie.shiro.hasher.HasherKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.system.model.User;
/**
 * 
 * @author 唐东宇
 *
 */
public class UserValidator extends Validator {
	
	protected Logger logger=LoggerFactory.getLogger(getClass());

	@Override
	protected void validate(Controller c) {
		// TODO Auto-generated method stub
		
		//ValidateKit.isNumber(oldpassword);
		if(getActionKey().equals("/user/updatePwd")){
			
			boolean oldpasswordEmpty=ValidateKit.isNullOrEmpty(c.getPara("oldpassword"));
			
			boolean passwordEmpty=ValidateKit.isNullOrEmpty(c.getPara("user.password"));
			
			
			//新密码不为空
			if (passwordEmpty) {
				addError("user_passwordMsg", "新密码不能为空");
			}
			//新密码格式
			else if (!passwordEmpty&&!ValidateKit.isPassword(c.getPara("user.password"))) {
				addError("user_passwordMsg", "新密码为英文字母 、数字和下划线长度为5-18");
			}
			//新密码与重复密码
			else if (!passwordEmpty&&!c.getPara("user.password").equals(c.getPara("repassword"))) {
				addError("user_passwordMsg", "重复密码不匹配");
			}
			
			//旧密码不为空
			else if(oldpasswordEmpty){
				addError("user_passwordMsg", "原始密码不能为空！");
			}
			
			else if (!oldpasswordEmpty) {
				User user=User.dao.findById(SubjectKit.getUser().get("id"));
				if (user!=null) {
					boolean match=HasherKit.match(c.getPara("oldpassword"), user.getStr("password"),Hasher.DEFAULT);
					if (!match) {
						addError("user_passwordMsg", "原始密码不匹配");
					}
				}else {
					addError("user_passwordMsg", "用户信息错误！");
				}
				
				
			}
			
		}else if(getActionKey().equals("/user/contactMe")){
			if(ValidateKit.isNullOrEmpty(c.getPara("name"))){
				addError("contactMsg","用户名不能为空");
			}else if(!ValidateKit.isPhone(c.getPara("phone"))){
				addError("contactMsg","电话号码格式不正确");
			}else if(ValidateKit.isNullOrEmpty(c.getPara("context"))){
				addError("contactMsg","提交建议不能为空");
			}
		}
		
	}

	@Override
	protected void handleError(Controller c) {
		// TODO Auto-generated method stub
		
		if(getActionKey().equals("/user/updatePwd")){
			c.keepModel(User.class);
			c.keepPara();
			c.render("pwd.html");
		}else if(getActionKey().equals("/user/contactMe")){
			c.renderJson("state", "提交内容出错！");
		}
		
	}

}
