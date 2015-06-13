package com.poicom.function.interceptor;

import org.apache.shiro.session.Session;

import cn.dreampie.shiro.core.SubjectKit;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.poicom.function.model.User;
/**
 * 
 * @author 唐东宇
 *
 */
public class CommonInterceptor implements Interceptor{

	@Override
	public void intercept(ActionInvocation ai) {
		if(ai.getActionKey().equals("/")){
			ai.invoke();
			ai.getController().setAttr("back", backParamUrl(ai));
		}else if(ai.getActionKey().equals("/report/reports")){
			ai.invoke();
			ai.getController().setAttr("back", backParamUrl(ai));
		}else if(ai.getActionKey().equals("/operate/operates")){
			ai.invoke();
			ai.getController().setAttr("back", backParamUrl(ai));
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
