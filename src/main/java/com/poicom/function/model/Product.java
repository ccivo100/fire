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
@TableBind(tableName="com_product")
public class Product extends BaseModel<Product>{
    /**
     * 
     */
    private static final long serialVersionUID = 7937778495057442680L;
    public static final Product dao=new Product();
    
    /**
     * 添加、更新缓存
     * @param id
     */
    public void cacheAdd(Long id){
        Product product = Product.dao.findById(id);
        CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_product + id, product);
    }
    
    /**
     * 移除缓存
     * @param id
     */
    public void cacheRemove(Long id){
        CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_product + id);
    }
    
    /**
     * 获取缓存对象
     * @param id
     * @return
     */
    public Product cacheGet(Long id ){
        Product product = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_product + id);
        return product;
    }
    
    public List<Product> getAllProduct(){
        return find("select * from com_product where deleted_at is null");
    }
    
    public Page<Product> findProductPage(int pageNumber, int pageSize,String where,String orderby,
            Object... paras){
        return paginateBy(pageNumber, pageSize, where+orderby, paras);
    }
    
    public List<Product> rootNode(String where,Object... paras){
		return findBy(where,paras);
	}

}
