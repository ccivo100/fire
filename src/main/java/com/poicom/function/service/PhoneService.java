package com.poicom.function.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.ObjectKit;
import com.poicom.basic.kit.StringKit;
import com.poicom.basic.kit.WebKit;
import com.poicom.basic.plugin.sqlxml.SqlXmlKit;
import com.poicom.function.model.Phone;
import com.poicom.function.model.PhoneType;
import com.poicom.function.model.User;

public class PhoneService extends BaseService {
    
    private static Logger log = Logger.getLogger(PhoneService.class);
    
    public static final PhoneService service = new PhoneService();
    
    /**
     * 新增
     * @param phone
     * @return
     */
    public Long save(Phone phone){
        phone.set("name", WebKit.delHTMLTag(phone.getStr("name")));
        phone.set("flag", "0");
        phone.save();
        return phone.getPKValue();
    }
    
    /**
     * 更新、设置不可用
     * @param phone
     */
    public void update(Phone phone){
        phone.set("name", WebKit.delHTMLTag(phone.getStr("name")));
        phone.update();
        Phone.dao.cacheAdd(phone.getPKValue());
    }
    
    /**
     * 删除
     * @param phoneid
     */
    public void drop(Long phoneid){
        Phone.dao.cacheRemove(phoneid);
        Phone.dao.deleteById(phoneid);
    }
    
    public boolean off(Phone phone){
        return phone.set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
    }

    public boolean on(Phone phone){
        return phone.set("deleted_at", null).update();
    }
    /**
     * 设置是否可运维
     * @param phone
     * @return
     */
    public boolean operate(Phone phone){
        if(phone.get("flag").equals("1")){
            phone.set("flag", "0");
            dropPhoneType(phone.getPKValue());
        }else if(phone.get("flag").equals("0")){
            phone.set("flag", "1");
        }
        return phone.update();
    }
    
    /**
     * 删除指定根部门及其子部门负责的运维类型
     * @param pid
     */
    private static void dropPhoneType(Long pid){
        List<Phone> childList= Phone.dao.findBy(" pid=?", pid);
        for(Phone child:childList){
            PhoneType.dao.dropBy(" phone_id=?", child.get("id"));
        }
        PhoneType.dao.dropBy(" phone_id=?", pid);
    }
    
    /**
     * 获取根节点
     * @return
     */
    public List<Phone> rootNode(){
        return Phone.dao.rootNode(" pid=? ",0);
    }
    
    /**
     * 递归获取子节点
     * @param phones
     */
    public void childNode(List<Phone> phones){
        for(Phone phone:phones){
            List<Phone> achild=Phone.dao.rootNode(" pid=? ",phone.get("id"));
            if(achild!=null){
                phone.setChildren(achild);
                //调用自身方法
                childNode(achild);
            }
        }
    }
    
    /**
     * 获得给定部门列表中，所有人员
     * @param phones
     * @return
     */
    public List<User> findUsers(List<Phone> phones){
        List<User> userList = new ArrayList<User>();
        for(Phone phone:phones){
            List<User> users = phone.getUsersById(phone.getPKValue());
            ObjectKit.formatUser1(users,phone);
            userList.addAll(users);
        }
        return userList;
    }
    
    /**
     * 在用户姓名后面加上部门名称
     * @param users
     * @param phone
     */
    public void formatUser(List<User> users,Phone phone){
        for(User user:users){
            user.set("full_name", user.get("full_name")+"（"+phone.getStr("name")+"）");
        }
    }
    
    public String childNodeId(Long phoneid){
        Phone phone = Phone.dao.findById(phoneid);
        List<Phone> phoneList = Phone.dao.find(SqlXmlKit.sql("phone.treeChildNode"), phone.getLong("pid"));
        String[] str = new String[phoneList.size()];
        for(int i=0;i<phoneList.size();i++) {
            str[i] = new String(phoneList.get(i).getLong("id").toString());
        }
        return StringKit.arrayToString(str);
    }
}
