package com.poicom.function.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import com.poicom.function.model.Alertinfo;
import com.poicom.function.service.AlertService;

import cn.dreampie.routebind.ControllerKey;

@ControllerKey(value = "/admin/alertinfo", path = "/app/admin")
public class AlertinfoController extends BaseController {
	
	private static Logger log = LoggerFactory.getLogger(AlertinfoController.class);
	
	private final static String ALERTINFO_QUERY_PAGE="alertinfo/query.html";
	
	public void index(){
		Page<Alertinfo> alertinfoPage;
		alertinfoPage=AlertService.service.page(getParaToInt(0,1), 10);
		setAttr("alertinfoPage",alertinfoPage);
		render("alertinfo.html");
	}
	
	public void query(){
		Long alertinfoid = getParaToLong("alertinfoid");
		Alertinfo alertinfo = Alertinfo.dao.findById(alertinfoid);
		renderJson("alertinfo",alertinfo);
	}
	
	public void delete(){
		Long alertinfoid = getParaToLong("alertinfoid");
		Alertinfo alertinfo = Alertinfo.dao.findById(alertinfoid);
		if(alertinfo.delete()){
			renderJson("state","删除成功！");
		}else
			renderJson("state","删除失败！");
	}

}
