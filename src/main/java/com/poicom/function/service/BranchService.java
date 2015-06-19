package com.poicom.function.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.WebKit;
import com.poicom.function.model.Apartment;
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
	
	/**
	 * 用于zTree，查询分公司分配部门
	 * @return
	 */
	public String treeNodeData(){
		
		List<Apartment> rootNode = Apartment.dao.rootNode();
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		
		int apartmentSize= rootNode.size();
		int apartmentIndexSize =apartmentSize-1;
		for(Apartment apartment:rootNode){
			sb.append(" { ");
			sb.append(" id : '").append(apartment.getPKValue()).append("', ");
			sb.append(" pId : '").append(apartment.get("pid")).append("', ");
			sb.append(" name : '").append(apartment.getStr("name")).append("', ");
			sb.append(" checked : false, ");
			sb.append(" font : {'font-weight':'bold'} ");
			sb.append(" }");
			
			List<Apartment> childNode = Apartment.dao.rootNode(" pid=? ",apartment.get("id"));
			int childapartmentSize= childNode.size();
			if ((rootNode.indexOf(apartment) < apartmentIndexSize)||childapartmentSize>0) {
				sb.append(", ");
			}
			for(Apartment child:childNode){
				sb.append(" { ");
				sb.append(" id : '").append(child.getPKValue()).append("', ");
				sb.append(" pId : '").append(child.get("pid")).append("', ");
				sb.append(" name : '").append(child.getStr("name")).append("', ");
				sb.append(" checked : false, ");
				sb.append(" font : {'font-weight':'bold'},");
				sb.append(" }");
				sb.append(", ");
			}
		}
		sb.append("]");
		
		return sb.toString();
	}

}
