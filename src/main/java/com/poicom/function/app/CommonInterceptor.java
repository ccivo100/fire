package com.poicom.function.app;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

public class CommonInterceptor implements Interceptor{

	@Override
	public void intercept(ActionInvocation ai) {
		if(ai.getActionKey().equals("/")){
			ai.invoke();
			ai.getController().setAttr("back", ai.getActionKey());
		}else if(ai.getActionKey().equals("/report/offer")){
			ai.invoke();
			ai.getController().setAttr("back", ai.getActionKey());
		}else if(ai.getActionKey().equals("/operate/deal")){
			ai.invoke();
			ai.getController().setAttr("back", ai.getActionKey());
		}else
			ai.invoke();
		
	}

}
