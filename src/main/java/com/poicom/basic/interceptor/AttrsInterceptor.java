package com.poicom.basic.interceptor;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import cn.dreampie.ValidateKit;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.poicom.basic.kit.WebKit;

public class AttrsInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		Controller c = ai.getController();
		
		Map<String,String[]> paraMap=c.getParaMap();
		Set<String> keys = paraMap.keySet();
		
		for(String key:keys){
			String[] values=paraMap.get(key);
			if(!ValidateKit.isNullOrEmpty(values)){
				for(int i=0;i<values.length;i++){
					values[i]=WebKit.getHTMLToString(values[i]);
				}
			}
			paraMap.put(key, values);
		}
		ai.invoke();

	}

}
