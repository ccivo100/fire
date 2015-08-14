package com.poicom.function.model;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.shiro.core.SubjectKit;
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
		User user = SubjectKit.getUser();
		UserInfo userinfo = user.getUserInfo();
		Apartment apartment = Apartment.dao.findById(userinfo.getLong("apartment_id"));
		
		this.set("apartmentid", apartment.getLong("pid"));
		this.set("created_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		this.set("created_userid", user.getLong("id"));
		
		return super.save();
	}
	
	public boolean update(){
		User user = SubjectKit.getUser();
		
		this.set("updated_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		this.set("updated_userid", user.getLong("id"));
		return super.update();
	}
	

}
