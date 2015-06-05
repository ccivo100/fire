package com.poicom.basic.interceptor;

import java.lang.reflect.Field;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.log.Logger;
import com.poicom.function.controller.BaseController;
import com.poicom.function.model.Permission;
import com.poicom.function.model.Syslog;

public class ParamPkgInterceptor implements Interceptor {
	
	private static Logger log = Logger.getLogger(ParamPkgInterceptor.class);

	@Override
	public void intercept(ActionInvocation ai) {
		BaseController controller = (BaseController) ai.getController();
		
		Class<?> controllerClass = controller.getClass();
		Class<?> superControllerClass = controllerClass.getSuperclass();
		
		Field[] fields = controllerClass.getDeclaredFields();
		Field[] parentFields = superControllerClass.getDeclaredFields();
		
		log.debug("*********************** 封装参数值到 controller 全局变量  start ***********************");
		
		Syslog reqSysLog = controller.getReqSysLog();
		Long operatorids = reqSysLog.getLong("operatorids");
		//Permission permission = Permission.dao.cacheGet(operatorids);//权限

	}

}
