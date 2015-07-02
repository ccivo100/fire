package com.poicom.function.model;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.dreampie.tablebind.TableBind;

@TableBind(tableName = "com_alertinfo")
public class Alertinfo extends BaseModel<Alertinfo> {

	private static final long serialVersionUID = 8981730605238407324L;
	
	public static final Alertinfo dao = new Alertinfo();
	
	public List<Alertinfo> list(String where,Object... paras){
		return find("select id,email,emailcontext,emailrecode,phone,smscontext,smsrecode,created_at from com_alertinfo where "+where, paras);
	}
	
	public Page<Alertinfo> page(int pageNumber,int pageSize,String where,Object... paras){
		
		return paginateBy(pageNumber, pageSize, where, paras);
	}
	
	

}
