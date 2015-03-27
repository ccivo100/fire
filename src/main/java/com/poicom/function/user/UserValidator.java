package com.poicom.function.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;
import cn.dreampie.shiro.core.SubjectKit;
import cn.dreampie.shiro.hasher.Hasher;
import cn.dreampie.shiro.hasher.HasherKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.user.model.User;

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
			if (!passwordEmpty&&!ValidateKit.isPassword(c.getPara("user.password"))) {
				addError("user_passwordMsg", "新密码为英文字母 、数字和下划线长度为5-18");
			}
			//新密码与重复密码
			if (!passwordEmpty&&!c.getPara("user.password").equals(c.getPara("repassword"))) {
				addError("user_passwordMsg", "重复密码不匹配");
			}
			
			//旧密码不为空
			if(oldpasswordEmpty){
				addError("user_passwordMsg", "原始密码不能为空！");
			}
			//旧密码格式
			if(!oldpasswordEmpty&&!ValidateKit.isPassword(c.getPara("oldpassword"))){
				addError("user_passwordMsg", "旧密码为英文字母、数字和下划线长度为5-18");
			}
			
			if (!oldpasswordEmpty) {
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
			
		}
		
	}

	@Override
	protected void handleError(Controller c) {
		// TODO Auto-generated method stub
		c.keepModel(User.class);
		c.keepPara();
		if(getActionKey().equals("/user/updatePwd")){
			c.forwardAction("/user/center");
		}
		
	}

}
