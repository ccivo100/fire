package com.poicom.function.controller;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.poicom.function.model.Template;
import com.poicom.function.service.TemplateService;

import cn.dreampie.routebind.ControllerKey;

@ControllerKey(value = "/admin/template", path="/app/admin")
public class TemplateController extends BaseController {
	
	private static Logger log = Logger.getLogger(TemplateController.class);
	
	private final static String TEMPLATE_INDEX_PAGE= "template/index.html";
	private final static String TEMPLATE_ADD_PAGE="template/add.html";
	private final static String TEMPLATE_EDIT_PAGE="template/edit.html";
	private final static String TEMPLATE_QUERY_PAGE="template/query.html";
	
	public void index(){
		
		Page<Template> templatePage = TemplateService.service.allTemplatePage(getParaToInt(0,1), 10);
		setAttr("templatePage", templatePage);
		
		render(TEMPLATE_INDEX_PAGE);
	}
	

}
