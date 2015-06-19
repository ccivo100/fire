package com.poicom.basic.interceptor;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.log.Logger;
import com.poicom.basic.common.SplitPage;
import com.poicom.function.controller.BaseController;
import com.poicom.function.model.Permission;
import com.poicom.function.model.Syslog;

/**
 * 参数封装拦截器
 * @author FireTercel 2015年6月17日 
 *
 */
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
	
	/**
	 * 分页参数处理
	 * @param controller
	 * @param superControllerClass
	 */
	public void splitPage(BaseController controller, Class<?> superControllerClass){
		SplitPage splitPage = new SplitPage();
		// 分页查询参数分拣
		Map<String, String> queryParam = new HashMap<String, String>();
		Enumeration<String> paramNames = controller.getParaNames();
		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement();
			if (name.startsWith("_query.")) {
				String value = controller.getPara(name);
				log.debug("分页，查询参数：name = " + name + " value = " + value);
				if(null != value){
					value = value.trim();
					if(!value.isEmpty()){
						String key = name.substring(7);
						queryParam.put(key, value);
					}
				}
			}
		}
		splitPage.setQueryParam(queryParam);
		
		String orderColunm = controller.getPara("orderColunm");// 排序条件
		if(null != orderColunm && !orderColunm.isEmpty()){
			log.debug("分页，排序条件：orderColunm = " + orderColunm);
			splitPage.setOrderColunm(orderColunm);
		}

		String orderMode = controller.getPara("orderMode");// 排序方式
		if(null != orderMode && !orderMode.isEmpty()){
			log.debug("分页，排序方式：orderMode = " + orderMode);
			splitPage.setOrderMode(orderMode);
		}

		String pageNumber = controller.getPara("pageNumber");// 第几页
		if(null != pageNumber && !pageNumber.isEmpty()){
			log.debug("分页，第几页：pageNumber = " + pageNumber);
			splitPage.setPageNumber(Integer.parseInt(pageNumber));
		}
		
		String pageSize = controller.getPara("pageSize");// 每页显示几多
		if(null != pageSize && !pageSize.isEmpty()){
			log.debug("分页，每页显示几多：pageSize = " + pageSize);
			splitPage.setPageSize(Integer.parseInt(pageSize));
		}
		
		controller.setSplitPage(splitPage);
	}

}
