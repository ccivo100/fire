package com.poicom.function.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.poicom.basic.common.DictKeys;
import com.poicom.basic.thread.ThreadParamInit;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.tree.TreeNode;
/**
 * 手机Model
 * @author 陈宇佳
 *
 */
@TableBind(tableName = "com_phone")
public class Phone extends BaseModel<Phone> implements TreeNode<Phone>{

    private static final long serialVersionUID = -6789094561247063469L;
    public static Phone dao=new Phone();
    
    @Override
    public long getId() {
        return this.getLong("id");
    }

    @Override
    public long getParentId() {
        return this.getLong("pid");
    }

    @Override
    public List<Phone> getChildren() {
        return this.get("children");
    }

    @Override
    public void setChildren(List<Phone> children) {
        this.put("children", children);
    }
    
    public Map<Long,Phone> getChild(){
        return this.get("child");
    }
    public void setChild(Map<Long,Phone> child){
        this.put("child", child);
    }
    
    /**
     * 添加、更新缓存
     * @param id
     */
    public void cacheAdd(Long id){
        Phone phone = Phone.dao.findById(id);
        CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_phone + id, phone);
    }
    
    /**
     * 移除缓存
     * @param id
     */
    public void cacheRemove(Long id){
        CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_phone + id);
    }
    
    /**
     * 获取缓存对象
     * @param id
     * @return
     */
    public Phone cacheGet(Long id ){
        Phone phone = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_phone + id);
        return phone;
    }
    
    
    public List<Phone> getAllPhone(){
        return find("select * from com_phone where deleted_at is null");
    }
    
    public Page<Phone> findPhonePage(int pageNumber, int pageSize,String where,String orderby,
            Object... paras){
        return paginateBy(pageNumber, pageSize, where+orderby, paras);
    }
    
    /**
     * @描述 根据type_id 获得phone的列表
     * @param paras
     * @return
     */
    public List<Phone> getPhonesList(Object... paras){
        return find(getSql("phone.findPhoneBySelect") + blank
                + getSql("phone.findPhoneByFrom"), paras);
    }
    
    /**
     * @描述 根据工单故障类型type_id，查其父类pid。排除已指派部门。
     * @param paras
     * @return 该工单未指派根部门list
     */
    public List<Phone> getPhonesListExcept(Object... paras){
        
        return find(
                "select phone.id,phone.name, phone.pid, phone.created_at,phone.updated_at,phone.deleted_at "
                        + "from com_phone as phone "
                        + "where phone.id in"
                        + "( select phonetype.phone_id "
                        + "from com_phone_type as phonetype "
                        + "where phonetype.type_id=? "
                        + "and phonetype.phone_id not in "
                        + getParaNumber_1(paras)
                        + ") and phone.deleted_at is null ", paras);
    }
    
    /**
     * @描述 根据故障类型&&父级部门，查询负责子部门
     * @param paras
     * @return
     */
    public List<Phone> getATPhonesList(Object... paras){
        return find(getSql("phone.ATPhones"),paras);
    }
    
    
    /**
     * @描述 获取根部门
     * @param paras
     * @return
     */
    public List<Phone> rootNode(String where,Object... paras){
        return findBy(where, paras);
    }
    
    public List<Phone> rootNode(Object... paras){
        return findBy(" pid=? and deleted_at is null  ", paras);
    }
    
    public List<Phone> rootNode(){
        return rootNode(" pid=? and deleted_at is null  ",0);
    }
    
    /**
     * 根据部门id查询该部门人员users，排除禁用账号。
     * @param phoneid
     * @return
     */
    public List<User> getUsersById(Long phoneid){
        return User.dao.find(getSql("user.userByPhoneId"), phoneid);
    }
    
    
}
