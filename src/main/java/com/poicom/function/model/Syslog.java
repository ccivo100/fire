package com.poicom.function.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.tablebind.TableBind;
/**
 * 
 * @author 陈宇佳
 *
 */
@TableBind(tableName="sec_syslog")
public class Syslog extends BaseModel<Syslog>{

	private static final long serialVersionUID = -8196136266285524310L;
	
	public static final Syslog syslog=new Syslog();
	
	protected Logger logger=LoggerFactory.getLogger(getClass());

}
