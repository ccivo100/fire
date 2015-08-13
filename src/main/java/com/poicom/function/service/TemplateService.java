package com.poicom.function.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Page;
import com.poicom.basic.kit.StringKit;
import com.poicom.basic.plugin.sqlxml.SqlXmlKit;
import com.poicom.function.model.Template;

/**
 * 模板业务层
 * @author FireTercel 2015年8月10日 
 *
 */
public class TemplateService extends BaseService {
	
	private static Logger log = LoggerFactory.getLogger(TemplateService.class);
	
	public static TemplateService service = new TemplateService();
	
	public Page<Template> allTemplatePage(int pageNumber,int pageSize , String where , Object... paras) {
		return Template.dao.paginate(pageNumber, pageSize, "select * ", "from com_template where 1=1 " + where , paras);
	}
	
	public void save(Template template, String[] receives){
		template.set("receive_userids", StringKit.arrayWithDot(receives));
		template.save();
	}
	
	public void update(Template template, String[] receives) {
		template.set("receive_userids", StringKit.arrayWithDot(receives));
		template.update();
	}
	/**
	 * 根据用户部门id
	 */
	public List<Template> findByApartmentId(Long apartmentid, Long typeid){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("apartmentid", "apartmentid");
		param.put("typeid", "typeid");
		return Template.dao.find(SqlXmlKit.sql("basic.template.byApartmentid", param), apartmentid, typeid);
		
	}
	

}
