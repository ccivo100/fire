package com.poicom.function.model;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poicom.basic.plugin.shiro.core.SubjectKit;

import cn.dreampie.tablebind.TableBind;

/**
 * 模板model
 * @author FireTercel 2015年8月10日 
 *
 */
@TableBind(tableName = "com_template")
public class Template extends BaseModel<Template> {
	private static final long serialVersionUID = -2786340993272001088L;
	private static Logger log = LoggerFactory.getLogger(Template.class);
	public static final Template dao = new Template();
	
	public boolean save(){
		this.set("created_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		this.set("created_userid", SubjectKit.getUser().getPKValue());
		
		return super.save();
	}
	
	public boolean update(){
		this.set("updated_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		this.set("updated_userid", SubjectKit.getUser().getPKValue());
		return super.update();
	}
	

}
