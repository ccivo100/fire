package com.poicom.function.app.model;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;
/**
 * 
 * @author 唐东宇
 *
 */
@TableBind(tableName = "com_level")
public class Level extends Model<Level> {

	private static final long serialVersionUID = 3548684854613386571L;
	
	public final static Level dao=new Level();
	

}
