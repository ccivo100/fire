package com.poicom.function.app.model;

import java.util.List;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;

@TableBind(tableName = "com_position")
public class Position extends Model<Position> {
	
	private static final long serialVersionUID = 3227952898435784635L;
	public static Position dao=new Position();
	
	public List<Position> getAllPosition(){
		return find("select * from com_position where deleted_at is null");
	}

}
