package com.poicom.function.controller;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.poicom.function.model.Branch;
import com.poicom.function.service.BranchService;
import com.poicom.function.validator.BranchValidator;

@ControllerKey(value = "/admin/branch", path="/app/admin")
public class BranchController extends BaseController {
	
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(BranchController.class);
	
	private final static String BRANCH_ADD_PAGE="branch/add.html";
	private final static String BRANCH_EDIT_PAGE="branch/edit.html";
	
	public void index(){
		Page<Branch> branchPage;
		String orderby=" ORDER BY branch.id ";
		if(ValidateKit.isNullOrEmpty(getPara("branch"))){
			String where = " 1=1 ";
			branchPage=Branch.dao.findBranchPage(getParaToInt(0,1), 10, where, orderby);
		}else{
			String where=" branch.name like ?  ";
			String condition ="%"+getPara("branch").trim()+"%";
			branchPage=Branch.dao.findBranchPage(getParaToInt(0,1), 10, where, orderby, condition);
			setAttr("branch",getPara("branch").trim());
		}
		setAttr("branchPage",branchPage);
		render("branch.html");
	}
	
	/**
	 * 准备新增单位
	 */
	public void add(){
		render(BRANCH_ADD_PAGE);
	}
	
	/**
	 * 新增单位
	 */
	@Before(BranchValidator.class)
	public void save(){
		Branch branch=getModel(Branch.class);
		BranchService.service.save(branch);
		redirect("/admin/branch");
	}
	
	/**
	 * 准备更新单位
	 */
	public void edit(){
		Branch branch=Branch.dao.findById(getPara("id"));
		setAttr("branch",branch);
		render(BRANCH_EDIT_PAGE);
	}
	
	/**
	 * 更新单位
	 */
	@Before(BranchValidator.class)
	public void update(){
		Branch branch=getModel(Branch.class);
		BranchService.service.update(branch);
		redirect("/admin/branch");
	}
	
	/**
	 * 设置不可用
	 */
	public void off(){
		BranchService.service.off(Branch.dao.findById(getPara("id")));
		redirect("/admin/branch");
	}
	
	/**
	 * 设置可用
	 */
	public void on(){
		BranchService.service.on(Branch.dao.findById(getPara("id")));
		redirect("/admin/branch");
	}
	
	public void treeData(){
		
		String json=BranchService.service.treeNodeData();
		renderJson(json);
	}

}
