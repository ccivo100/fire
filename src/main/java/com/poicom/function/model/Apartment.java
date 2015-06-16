package com.poicom.function.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.poicom.basic.common.DictKeys;
import com.poicom.basic.thread.ThreadParamInit;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.tree.TreeNode;
/**
 * 部门Model
 * @author 唐东宇
 *
 */
@TableBind(tableName = "com_apartment")
public class Apartment extends BaseModel<Apartment> implements TreeNode<Apartment>{

	private static final long serialVersionUID = 5493122811350290559L;
	public static Apartment dao=new Apartment();
	
	@Override
	public long getId() {
		return this.getLong("id");
	}

	@Override
	public long getParentId() {
		return this.getLong("pid");
	}

	@Override
	public List<Apartment> getChildren() {
		return this.get("children");
	}

	@Override
	public void setChildren(List<Apartment> children) {
		this.put("children", children);
	}
	
	public Map<Long,Apartment> getChild(){
		return this.get("child");
	}
	public void setChild(Map<Long,Apartment> child){
		this.put("child", child);
	}
	
	/**
	 * 添加、更新缓存
	 * @param id
	 */
	public void cacheAdd(Long id){
		Apartment apartment = Apartment.dao.findById(id);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_apartment + id, apartment);
	}
	
	/**
	 * 移除缓存
	 * @param id
	 */
	public void cacheRemove(Long id){
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_apartment + id);
	}
	
	/**
	 * 获取缓存对象
	 * @param id
	 * @return
	 */
	public Apartment cacheGet(Long id ){
		Apartment apartment = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_apartment + id);
		return apartment;
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
		return find(getSql("apartment.findApartmentBySelect") + blank
				+ getSql("apartment.findApartmentByFrom"), paras);
	}
	
	/**
	 * @描述 根据工单故障类型type_id，查其父类pid。排除已指派部门。
	 * @param paras
	 * @return 该工单未指派根部门list
	 */
	public List<Apartment> getApartmentsListExcept(Object... paras){
		
		return find(
				"select apartment.id,apartment.name, apartment.pid, apartment.created_at,apartment.updated_at,apartment.deleted_at "
						+ "from com_apartment as apartment "
						+ "where apartment.id in"
						+ "( select apartmenttype.apartment_id "
						+ "from com_apartment_type as apartmenttype "
						+ "where apartmenttype.type_id=? "
						+ "and apartmenttype.apartment_id not in "
						+ getParaNumber_1(paras)
						+ ") and apartment.deleted_at is null ", paras);
	}
	
	/**
	 * @描述 根据故障类型&&父级部门，查询负责子部门
	 * @param paras
	 * @return
	 */
	public List<Apartment> getATApartmentsList(Object... paras){
		return find(getSql("apartment.ATApartments"),paras);
	}
	
	
	
	/**
	 * @描述 获取根部门
	 * @param paras
	 * @return
	 */
	public List<Apartment> rootNode(String where,Object... paras){
		return findBy(where, paras);
	}
	

}
