package com.poicom.function.model;

import java.util.List;

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
	 * @描述 获取根部门
	 * @param paras
	 * @return
	 */
	public List<Apartment> findApartmentsByPid(String where,Object... paras){
		return findBy(where, paras);
	}

	

}
