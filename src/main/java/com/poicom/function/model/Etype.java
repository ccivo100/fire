package com.poicom.function.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.tree.TreeNode;


/**
 * @描述 故障类型Model
 * @author 陈宇佳
 *
 */
@TableBind(tableName = "com_type")
public class Etype extends BaseModel<Etype> implements TreeNode<Etype>{
	
	private static final long serialVersionUID = 3010321427941080031L;
	public final static Etype dao=new Etype();
	
	@Override
	public long getId() {
		
		return this.getLong("id");
	}


	@Override
	public long getParentId() {
		return this.getLong("pid");
	}


	@Override
	public List<Etype> getChildren() {
		return this.get("children");
	}


	@Override
	public void setChildren(List<Etype> children) {
		this.put("children", children);
	}
	
	
	/**
	 * 获得所有故障类型（通用）
	 * @return
	 */
	public List<Etype> getAllType(){
		return Etype.dao.find("select * from com_type where deleted_at is null");
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
		return Db.find(getSql("type.findOperatorTypeBySelect") + blank
				+ getSql("type.findOperatorTypeByFrom"), paras);
	}
	
	/**
	 * @描述 获取运维部门 处理的故障类型列表
	 * @param paras
	 * @return
	 */
	public List<Record> findApartmentType(Object... paras){
		return Db.find(getSql("type.findApartmentTypeBySelect") + blank
				+ getSql("type.findApartmentTypeByFrom"), paras);
	}
	
	public Page<Etype> findErrorTypePage(int pageNumber, int pageSize,String where,String orderby,
			Object... paras){
		return paginateBy(pageNumber, pageSize, where+orderby, paras);
	}

	/**
	 * 获取根-故障类型
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<Etype> rootNode(String where,Object... paras){
		return findBy(where,paras);
	}
	
	/**
	 * 获取子节点-故障类型
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<Etype> childNode(String where,Object... paras){
		return findBy(where,paras);
	}

}
