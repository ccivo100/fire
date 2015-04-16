package com.poicom.function.app;

import org.apache.shiro.session.Session;

import cn.dreampie.shiro.core.SubjectKit;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.poicom.function.system.model.User;

public class CommonInterceptor implements Interceptor{

	@Override
	public void intercept(ActionInvocation ai) {
		if(ai.getActionKey().equals("/")){
			ai.invoke();
			ai.getController().setAttr("back", backParamUrl(ai));
		}else if(ai.getActionKey().equals("/report/query")){
			ai.invoke();
			ai.getController().setAttr("back", backParamUrl(ai));
		}else if(ai.getActionKey().equals("/operate/deal")){
			ai.invoke();
			ai.getController().setAttr("back", backParamUrl(ai));
		}else if(ai.getActionKey().equals("/signin")){
			ai.invoke();
			//User user=SubjectKit.getUser();
			//ai.getController().setSessionAttr("current_user", user);
		}else if(ai.getActionKey().equals("/signout")){
			//ai.getController().removeSessionAttr("current_user");
			ai.invoke();
		}else{
			ai.invoke();
		}
	}
	
	public static String backParamUrl(ActionInvocation ai){
		if(ai.getController().getPara()==null){
			return ai.getActionKey();
		}else{
			return ai.getActionKey()+"/"+ai.getController().getPara();
		}
		
		
	}

}
