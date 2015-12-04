package com.poicom.function.model;

import cn.dreampie.tablebind.TableBind;
/**
 * 
 * @author 陈宇佳
 *
 */
@TableBind(tableName = "com_level")
public class Level extends BaseModel<Level> {

	private static final long serialVersionUID = 3548684854613386571L;
	
	public final static Level dao=new Level();
	

}
