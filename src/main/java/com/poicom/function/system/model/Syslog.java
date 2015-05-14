package com.poicom.function.system.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;
/**
 * 
 * @author 唐东宇
 *
 */
@TableBind(tableName="sec_syslog")
public class Syslog extends Model<Syslog>{

	private static final long serialVersionUID = -8196136266285524310L;
	
	public static final Syslog syslog=new Syslog();
	
	protected Logger logger=LoggerFactory.getLogger(getClass());

}
