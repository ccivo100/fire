package com.poicom.function.app.model;

import java.util.List;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;

@TableBind(tableName="com_branch")
public class Branch extends Model<Branch>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5240642720729773525L;
	public static Branch dao=new Branch();
	
	public List<Branch> getAllBranch(){
		return find("select * from com_branch where deleted_at is null");
	}
	

}
