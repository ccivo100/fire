package com.poicom.function.service;

import org.joda.time.DateTime;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.WebKit;
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

}
