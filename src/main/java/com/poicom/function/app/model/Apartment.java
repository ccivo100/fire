package com.poicom.function.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.dreampie.sqlinxml.SqlKit;
import cn.dreampie.tablebind.TableBind;
import cn.dreampie.tree.TreeNode;
import cn.dreampie.web.model.Model;

@TableBind(tableName = "com_apartment")
public class Apartment extends Model<Apartment> implements TreeNode<Apartment>{

	private static final long serialVersionUID = 5493122811350290559L;
	public static Apartment dao=new Apartment();
	
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return this.getLong("id");
	}

	@Override
	public long getParentId() {
		// TODO Auto-generated method stub
		return this.getLong("pid");
	}

	@Override
	public List<Apartment> getChildren() {
		// TODO Auto-generated method stub
		return this.get("children");
	}

	@Override
	public void setChildren(List<Apartment> children) {
		// TODO Auto-generated method stub
		this.put("children", children);
	}
	
	public List<Apartment> getAllApartment(){
		return find("select * from com_apartment where deleted_at is null");
	}
	
	public Page<Apartment> findApartmentPage(int pageNumber, int pageSize,String where,String orderby,
			Object... paras){
		return paginateBy(pageNumber, pageSize, where+orderby, paras);
	}
	
	/**
	 * @描述 根据type_id 获得apartment的列表
	 * @param paras
	 * @return
	 */
	public List<Apartment> getApartmentsList(Object... paras){
		return find(SqlKit.sql("apartment.findApartmentBySelect") + blank
				+ SqlKit.sql("apartment.findApartmentByFrom"), paras);
	}
	
	/**
	 * @描述 获取根部门
	 * @param paras
	 * @return
	 */
	public List<Apartment> findApartmentsByPid(Object... paras){
		return findBy(" pid=? ", paras);
	}

	

}
