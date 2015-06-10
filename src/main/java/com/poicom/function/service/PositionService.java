package com.poicom.function.service;

import org.joda.time.DateTime;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.WebKit;
import com.poicom.function.model.Apartment;
import com.poicom.function.model.Position;

public class PositionService extends BaseService {
	
	private static Logger log = Logger.getLogger(PositionService.class);
	
	public static final PositionService service = new PositionService();
	
	/**
	 * 新增
	 * @param branch
	 * @return
	 */
	public Long save(Position position){
		position.set("name", WebKit.delHTMLTag(position.getStr("name")));
		position.set("apartment_name", Apartment.dao.findById(position.getLong("apartment_id")).get("name"));
		position.save();
		return position.getPKValue();
	}
	
	/**
	 * 更新、设置不可用
	 * @param branch
	 */
	public void update(Position position){
		position.set("name", WebKit.delHTMLTag(position.getStr("name")));
		position.set("apartment_name", Apartment.dao.findById(position.getLong("apartment_id")).get("name"));
		position.update();
		Position.dao.cacheAdd(position.getPKValue());
	}
	
	/**
	 * 删除
	 * @param branchid
	 */
	public void drop(Long positionid){
		Position.dao.cacheRemove(positionid);
		Position.dao.deleteById(positionid);
	}
	
	public boolean off(Position position){
		return position.set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
	}
	
	public boolean on(Position position){
		return position.set("deleted_at", null).update();
	}

}
