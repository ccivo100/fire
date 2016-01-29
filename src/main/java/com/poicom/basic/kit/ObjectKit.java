package com.poicom.basic.kit;

import java.util.List;

import com.poicom.function.model.Apartment;
import com.poicom.function.model.Phone;
import com.poicom.function.model.User;
import com.poicom.function.model.UserInfo;

public class ObjectKit {
    
    public static void formatUser(List<User> users){
        for(User user:users){
            UserInfo userinfo = user.getUserInfo();
            Apartment apartment = Apartment.dao.findById(userinfo.getLong("apartment_id"));
            user.set("full_name", user.get("full_name")+"（"+apartment.getStr("name")+"）");
        }
    }
    
    /**
     * 在用户姓名后面加上部门名称
     * @param users
     * @param apartment
     */
    public static void formatUser(List<User> users, Apartment apartment){
        for(User user:users){
            user.set("full_name", user.get("full_name")+"（"+apartment.getStr("name")+"）");
        }
    }


    public static void formatUser1(List<User> users){
        for(User user:users){
            UserInfo userinfo = user.getUserInfo();
            Phone phone = Phone.dao.findById(userinfo.getLong("phone_id"));
            user.set("full_name", user.get("full_name")+"（"+phone.getStr("name")+"）");
        }
    }
    
    /**
     * 在用户姓名后面加上部门名称
     * @param users
     * @param apartment
     */
    public static void formatUser1(List<User> users, Phone phone){
        for(User user:users){
            user.set("full_name", user.get("full_name")+"（"+phone.getStr("name")+"）");
        }
    }

}
