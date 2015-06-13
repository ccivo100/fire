package com.poicom.function.service;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.WebKit;
import com.poicom.function.model.ApartmentType;
import com.poicom.function.model.Etype;

public class EtypeService extends BaseService {
	
	private static Logger log = Logger.getLogger(EtypeService.class);
	
	public static final EtypeService service = new EtypeService();
	
	public Long save(Etype etype){
		etype.set("name", WebKit.delHTMLTag(etype.getStr("name")));
		etype.save();
		return etype.getPKValue();
	}
	
	public void update(Etype etype){
		etype.set("name", WebKit.delHTMLTag(etype.getStr("name")));
		etype.update();
	}
	
	public void drop(Long etypeid){
		Etype.dao.deleteById(etypeid);
	}
	
	public boolean off(Etype etype){
		return etype.set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
	}
	
	public boolean on(Etype etype){
		return etype.set("deleted_at", null).update();
	}
	
	/**
	 * 获取根节点
	 * @return
	 */
	public List<Etype> rootNode(){
		return Etype.dao.rootNode(" pid=? ", 0);
	}
	
	
	/**
	 * 递归获取子节点-形成树形结构
	 * @param etypes
	 */
	public void childNode(List<Etype> etypes){
		for(Etype etype:etypes){
			List<Etype> echild = Etype.dao.rootNode(" pid=? ", etype.get("id"));
			if(echild != null){
				etype.setChildren(echild);
				childNode(echild);
			}
		}
	}
	
	public List<Etype> childNode(Long pid){
		List<ApartmentType> apartmenttype=ApartmentType.dao.findBy("apartmentType.apartment_id=?",pid);
		List<Etype> allChildList=new LinkedList<Etype>();
		for(ApartmentType at:apartmenttype){
			Long typeid=at.get("type_id");
			List<Etype> childList = Etype.dao.childNode(" pid=? and deleted_at is null ", typeid);
			allChildList.addAll(childList);
		}
		return allChildList;
	}

}
