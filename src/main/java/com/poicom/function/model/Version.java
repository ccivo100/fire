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
@TableBind(tableName = "com_version")
public class Version extends BaseModel<Version> {
    
	private static final long serialVersionUID = -8163801619844236132L;
    public static Version dao=new Version();
    
    /**
     * 添加、更新缓存
     * @param id
     */
    public void cacheAdd(Long id){
        Version version = Version.dao.findById(id);
        CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_version + id, version);
    }
    
    /**
     * 移除缓存
     * @param id
     */
    public void cacheRemove(Long id){
        CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_version + id);
    }
    
    /**
     * 获取缓存对象
     * @param id
     * @return
     */
    public Version cacheGet(Long id ){
        Version version = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_version + id);
        return version;
    }
    
    public List<Version> getAllVersion(){
        return find("select * from com_version where deleted_at is null");
    }
    
    public List<Version> versionByPhoneId(Object paras){
        return findBy(" phone_id=? and deleted_at is null", paras);
    }
    
    public Page<Version> findVersionPage(int pageNumber, int pageSize,String where,String orderby,
            Object... paras){
        return paginateBy(pageNumber, pageSize, where+orderby, paras);
    }

}
