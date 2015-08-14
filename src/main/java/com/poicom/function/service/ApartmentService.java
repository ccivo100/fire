package com.poicom.function.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.StringKit;
import com.poicom.basic.kit.WebKit;
import com.poicom.basic.plugin.sqlxml.SqlXmlKit;
import com.poicom.function.model.Apartment;
import com.poicom.function.model.ApartmentType;
import com.poicom.function.model.User;

public class ApartmentService extends BaseService {
	
	private static Logger log = Logger.getLogger(ApartmentService.class);
	
	public static final ApartmentService service = new ApartmentService();
	
	/**
	 * 新增
	 * @param apartment
	 * @return
	 */
	public Long save(Apartment apartment){
		apartment.set("name", WebKit.delHTMLTag(apartment.getStr("name")));
		apartment.set("flag", "0");
		apartment.save();
		return apartment.getPKValue();
	}
	
	/**
	 * 更新、设置不可用
	 * @param apartment
	 */
	public void update(Apartment apartment){
		apartment.set("name", WebKit.delHTMLTag(apartment.getStr("name")));
		apartment.update();
		Apartment.dao.cacheAdd(apartment.getPKValue());
	}
	
	/**
	 * 删除
	 * @param apartmentid
	 */
	public void drop(Long apartmentid){
		Apartment.dao.cacheRemove(apartmentid);
		Apartment.dao.deleteById(apartmentid);
	}
	
	public boolean off(Apartment apartment){
		return apartment.set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
	}

	public boolean on(Apartment apartment){
		return apartment.set("deleted_at", null).update();
	}
	/**
	 * 设置是否可运维
	 * @param apartment
	 * @return
	 */
	public boolean operate(Apartment apartment){
		if(apartment.get("flag").equals("1")){
			apartment.set("flag", "0");
			dropApartmentType(apartment.getPKValue());
		}else if(apartment.get("flag").equals("0")){
			apartment.set("flag", "1");
		}
		return apartment.update();
	}
	
	/**
	 * 删除指定根部门及其子部门负责的运维类型
	 * @param pid
	 */
	private static void dropApartmentType(Long pid){
		List<Apartment> childList= Apartment.dao.findBy(" pid=?", pid);
		for(Apartment child:childList){
			ApartmentType.dao.dropBy(" apartment_id=?", child.get("id"));
		}
		ApartmentType.dao.dropBy(" apartment_id=?", pid);
	}
	
	/**
	 * 获取根节点
	 * @return
	 */
	public List<Apartment> rootNode(){
		return Apartment.dao.rootNode(" pid=? ",0);
	}
	
	/**
	 * 递归获取子节点
	 * @param apartments
	 */
	public void childNode(List<Apartment> apartments){
		for(Apartment apartment:apartments){
			List<Apartment> achild=Apartment.dao.rootNode(" pid=? ",apartment.get("id"));
			if(achild!=null){
				apartment.setChildren(achild);
				//调用自身方法
				childNode(achild);
			}
		}
	}
	
	public List<User> findUsers(List<Apartment> apartments){
		List<User> userList = new ArrayList<User>();
		for(Apartment apartment:apartments){
			List<User> users = apartment.getUsersById(apartment.getPKValue());
			userList.addAll(users);
		}
		return userList;
	}
	
	public String childNodeId(Long apartmentid){
		Apartment apartment = Apartment.dao.findById(apartmentid);
		List<Apartment> apartmentList = Apartment.dao.find(SqlXmlKit.sql("apartment.treeChildNode"), apartment.getLong("pid"));
		String[] str = new String[apartmentList.size()];
		for(int i=0;i<apartmentList.size();i++) {
			str[i] = new String(apartmentList.get(i).getLong("id").toString());
		}
		return StringKit.arrayToString(str);
	}
}
