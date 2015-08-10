package com.poicom.function.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Page;
import com.poicom.function.model.Template;

/**
 * 模板业务层
 * @author FireTercel 2015年8月10日 
 *
 */
public class TemplateService extends BaseService {
	
	private static Logger log = LoggerFactory.getLogger(TemplateService.class);
	
	public static TemplateService service = new TemplateService();
	
	public Page<Template> allTemplatePage(int pageNumber,int pageSize ,Object... paras) {
		return Template.dao.paginate(pageNumber, pageSize, "", "", paras);
	}
	

}
