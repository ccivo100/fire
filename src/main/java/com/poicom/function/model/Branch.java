package com.poicom.function.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.poicom.basic.common.DictKeys;
import com.poicom.basic.thread.ThreadParamInit;

import cn.dreampie.tablebind.TableBind;
/**
 * 
 * @author 陈宇佳
 *
 */
@TableBind(tableName="com_branch")
public class Branch extends BaseModel<Branch>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5240642720729773525L;
	public static final Branch dao=new Branch();
	
	/**
	 * 添加、更新缓存
	 * @param id
	 */
	public void cacheAdd(Long id){
		Branch branch = Branch.dao.findById(id);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_branch + id, branch);
	}
	
	/**
	 * 移除缓存
	 * @param id
	 */
	public void cacheRemove(Long id){
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_branch + id);
	}
	
	/**
	 * 获取缓存对象
	 * @param id
	 * @return
	 */
	public Branch cacheGet(Long id ){
		Branch branch = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_branch + id);
		return branch;
	}
	
	public List<Branch> getAllBranch(){
		return find("select * from com_branch where deleted_at is null");
	}
	
	public Page<Branch> findBranchPage(int pageNumber, int pageSize,String where,String orderby,
			Object... paras){
		return paginateBy(pageNumber, pageSize, where+orderby, paras);
	}
	

}
