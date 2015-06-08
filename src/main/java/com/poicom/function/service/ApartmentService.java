package com.poicom.function.service;

import org.joda.time.DateTime;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.WebKit;
import com.poicom.function.model.Apartment;

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
}
