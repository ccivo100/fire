package com.poicom.function.app;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

public class CommonInterceptor implements Interceptor{

	@Override
	public void intercept(ActionInvocation ai) {
		if(ai.getActionKey().equals("/")){
			ai.invoke();
			ai.getController().setAttr("back", backParamUrl(ai));
		}else if(ai.getActionKey().equals("/report/offer")){
			ai.invoke();
			ai.getController().setAttr("back", backParamUrl(ai));
		}else if(ai.getActionKey().equals("/operate/deal")){
			ai.invoke();
			ai.getController().setAttr("back", backParamUrl(ai));
		}else
			ai.invoke();
		
	}
	
	public static String backParamUrl(ActionInvocation ai){
		if(ai.getController().getPara()==null){
			return ai.getActionKey();
		}else{
			return ai.getActionKey()+"/"+ai.getController().getPara();
		}
		
		
	}

}
