package com.poicom.function.app.model;

import java.util.List;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;


/**
 * @描述 故障类型Model
 * @author poicom7
 *
 */
@TableBind(tableName = "com_type")
public class ErrorType extends Model<ErrorType>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3010321427941080031L;
	public static ErrorType dao=new ErrorType();
	
	
	/**
	 * 获得所有故障类型（通用）
	 * @return
	 */
	public List<ErrorType> getAllType(){
		return ErrorType.dao.find("select * from com_type");
	}

}
