package com.poicom.function.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.poicom.basic.common.DictKeys;
import com.poicom.basic.thread.ThreadParamInit;

import cn.dreampie.tablebind.TableBind;
/**
 * 
 * @author 唐东宇
 *
 */
@TableBind(tableName = "com_position")
public class Position extends BaseModel<Position> {
	
	private static final long serialVersionUID = 3227952898435784635L;
	public static Position dao=new Position();
	
	/**
	 * 添加、更新缓存
	 * @param id
	 */
	public void cacheAdd(Long id){
		Position position = Position.dao.findById(id);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_position + id, position);
	}
	
	/**
	 * 移除缓存
	 * @param id
	 */
	public void cacheRemove(Long id){
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_position + id);
	}
	
	/**
	 * 获取缓存对象
	 * @param id
	 * @return
	 */
	public Position cacheGet(Long id ){
		Position position = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_position + id);
		return position;
	}
	
	public List<Position> getAllPosition(){
		return find("select * from com_position where deleted_at is null");
	}
	
	public List<Position> positionByApartmentId(Object paras){
		return findBy(" apartment_id=? and deleted_at is null", paras);
	}
	
	public Page<Position> findPositionPage(int pageNumber, int pageSize,String where,String orderby,
			Object... paras){
		return paginateBy(pageNumber, pageSize, where+orderby, paras);
	}

}
