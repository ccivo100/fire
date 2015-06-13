package com.poicom.function.service;

import org.joda.time.DateTime;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.WebKit;
import com.poicom.function.model.Branch;

public class BranchService extends BaseService {
	
	private static Logger log = Logger.getLogger(BranchService.class);
	
	public static final BranchService service = new BranchService();
	
	/**
	 * 新增
	 * @param branch
	 * @return
	 */
	public Long save(Branch branch){
		branch.set("name", WebKit.delHTMLTag(branch.getStr("name")));
		branch.save();
		return branch.getPKValue();
	}
	
	/**
	 * 更新、设置不可用
	 * @param branch
	 */
	public void update(Branch branch){
		branch.set("name", WebKit.delHTMLTag(branch.getStr("name")));
		branch.update();
		Branch.dao.cacheAdd(branch.getPKValue());
	}
	
	/**
	 * 删除
	 * @param branchid
	 */
	public void drop(Long branchid){
		Branch.dao.cacheRemove(branchid);
		Branch.dao.deleteById(branchid);
	}
	
	public boolean off(Branch branch){
		return branch.set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
	}
	
	public boolean on(Branch branch){
		return branch.set("deleted_at", null).update();
	}

}
