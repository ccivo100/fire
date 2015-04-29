package com.poicom.function.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.dreampie.sqlinxml.SqlKit;
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
		return ErrorType.dao.find("select * from com_type where deleted_at is null");
	}
	
	/**
	 * @描述 获取用户id为paras故障类型中间表
	 * @param paras
	 * @return
	 */
	public List<Record> getTypeByUser(Object... paras){
		return Db
				.find("select cut.id id,cut.user_id userid,cut.type_id typeid from com_user_type as cut where cut.user_id=?",
						paras);
	}
	
	/**
	 * @描述 获取运维人员处理故障类型列表
	 * @param paras
	 * @return
	 */
	public List<Record> getOperatorType(Object... paras){
		return Db.find(SqlKit.sql("type.findOperatorTypeBySelect") + blank
				+ SqlKit.sql("type.findOperatorTypeByFrom"), paras);
	}
	
	/**
	 * @描述 获取运维部门 处理的故障类型列表
	 * @param paras
	 * @return
	 */
	public List<Record> findApartmentType(Object... paras){
		return Db.find(SqlKit.sql("type.findApartmentTypeBySelect")+blank
				+SqlKit.sql("type.findApartmentTypeByFrom"), paras);
	}
	
	public Page<ErrorType> findErrorTypePage(int pageNumber, int pageSize,String where,String orderby,
			Object... paras){
		return paginateBy(pageNumber, pageSize, where+orderby, paras);
	}

}
