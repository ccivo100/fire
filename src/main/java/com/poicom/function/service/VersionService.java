package com.poicom.function.service;

import org.joda.time.DateTime;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.WebKit;
import com.poicom.function.model.Phone;
import com.poicom.function.model.Version;

public class VersionService extends BaseService {
    
    private static Logger log = Logger.getLogger(VersionService.class);
    
    public static final VersionService service = new VersionService();
    
    /**
     * 新增
     * @param product
     * @return
     */
    public Long save(Version version){
        version.set("name", WebKit.delHTMLTag(version.getStr("name")));
        version.set("phone_name", Phone.dao.findById(version.getLong("phone_id")).get("name"));
        version.save();
        return version.getPKValue();
    }
    
    /**
     * 更新、设置不可用
     * @param product
     */
    public void update(Version version){
        version.set("name", WebKit.delHTMLTag(version.getStr("name")));
        version.set("phone_name", Phone.dao.findById(version.getLong("phone_id")).get("name"));
        version.update();
        Version.dao.cacheAdd(version.getPKValue());
    }
    
    /**
     * 删除
     * @param productid
     */
    public void drop(Long versionid){
        Version.dao.cacheRemove(versionid);
        Version.dao.deleteById(versionid);
    }
    
    public boolean off(Version version){
        return version.set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
    }
    
    public boolean on(Version version){
        return version.set("deleted_at", null).update();
    }

}
