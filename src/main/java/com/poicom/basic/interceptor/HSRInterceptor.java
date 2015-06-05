package com.poicom.basic.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.poicom.basic.kit.HSRequestKit;

public class HSRInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		new HSRequestKit(ai.getController().getRequest());
		ai.invoke();
	}

}
