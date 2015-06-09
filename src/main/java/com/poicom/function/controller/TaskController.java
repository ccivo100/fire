package com.poicom.function.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.poicom.function.model.Apartment;
import com.poicom.function.model.ApartmentType;
import com.poicom.function.model.Etype;
import com.poicom.function.service.EtypeService;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;

@ControllerKey(value="/admin/task",path="/app/admin")
public class TaskController extends BaseController {
	
	private static Logger log = Logger.getLogger(TaskController.class);
	
	private final static String TASK_ROOT_PAGE="type/rootTask.html";
	private final static String TASK_CHILD_PAGE="type/childTask.html";
	
	public void rootTask(){
		String where =" apartment.flag=? and apartment.deleted_at is null ";
		String orderby = " order by apartment.id ";
		
		List<Apartment> rootApartmentList = Apartment.dao.findBy(where+orderby, "1");
		for(Apartment root:rootApartmentList){
			List <Record> list=Etype.dao.findApartmentType(root.get("id"));
			root.set("remark", list);
		}
		setAttr("rootApartmentList",rootApartmentList);
		setAttr("typeList",Etype.dao.rootNode(" pid=? and deleted_at is null ", 0));
		
		render(TASK_ROOT_PAGE);
	}
	
	/**
	 * @描述 执行分配操作
	 */
	@Before(Tx.class)
	public void rootAssign(){
		
		//前台选中的 类型s
		String[] types =getParaValues("types");
		
		List<ApartmentType> apartmenttype=ApartmentType.dao.findBy("apartmentType.apartment_id=?",getParaToLong("id"));
		
		if(ValidateKit.isNullOrEmpty(types)){
			for(int i=0;i<apartmenttype.size();i++){
				System.out.println(apartmenttype.get(i).getLong("type_id")+" 已取消，执行删除操作！");
				apartmenttype.get(i).delete();
			}
		}else if(ValidateKit.isNullOrEmpty(apartmenttype)){
			for(int i=0;i<types.length;i++){
				System.out.println(types[i]+" 不存在，执行新增操作！");
				ApartmentType at=new ApartmentType().set("apartment_id",  getPara("id")).set("type_id", types[i]).set("level", "1");
				at.save();
			}
			
		}else{
			//需要处理该故障类型?，不存在则新增。
			for(int i=0;i<types.length;i++){
				boolean flag=false;
				for(int j=0;j<apartmenttype.size();j++){
					if(Integer.parseInt(types[i])==apartmenttype.get(j).getLong("type_id")){
						flag=true;
						break;
					}
				}
				if(flag){
					System.out.println(types[i]+" 存在，保留不删除！");
				}else if(!flag){
					System.out.println(types[i]+" 不存在，执行新增操作！");
					ApartmentType at=new ApartmentType().set("apartment_id",  getPara("id")).set("type_id", types[i]).set("level", "1");
					at.save();
				}
			}
			
			//需要保留该故障类型?，不保留则删除
			for(int i=0;i<apartmenttype.size();i++){
				boolean flag=false;
				for(int j=0;j<types.length;j++){
					if(apartmenttype.get(i).getLong("type_id")==Integer.parseInt(types[j])){
						flag=true;
						break;
					}
				}
				if(flag){
					System.out.println(apartmenttype.get(i).getLong("type_id")+" 未取消，保留不删除！");
				}else if(!flag){
					System.out.println(apartmenttype.get(i).getLong("type_id")+" 已取消，执行删除操作！");
					apartmenttype.get(i).delete();
				}
			}
			
		}
		redirect("/admin/task/rootTask");
		
	}
	
	/**
	 * @描述 二级部门分配运维任务。
	 */
	public void childTask(){
		String where=" apartment.pid=?  and apartment.deleted_at is null ";
		String orderby=" ORDER BY apartment.id ";
		//二级部门的父id
		Long pid =getParaToLong("id");
		//获得指定部门的二级部门
		Page<Apartment> apartmentPage=Apartment.dao.findApartmentPage(getParaToInt(0,1), 10, where, orderby, pid);
		
		//各二级部门已分配任务
		for(int i=0;i<apartmentPage.getList().size();i++){
			List <Record> list=Etype.dao.findApartmentType(apartmentPage.getList().get(i).get("id"));
			apartmentPage.getList().get(i).set("remark", list);
		}
		
		setAttr("apartmentPage",apartmentPage);
		//根据一级部门分配的故障总类，获得所有故障细类
		setAttr("typeList",EtypeService.service.childNode(pid));
		
		render(TASK_CHILD_PAGE);
	}
	
	/**
	 * @描述 执行分配操作
	 */
	@Before(Tx.class)
	public void childAssign(){
		
		//前台选中的 类型s
		String[] types =getParaValues("types");
		
		List<ApartmentType> apartmenttype=ApartmentType.dao.findBy("apartmentType.apartment_id=?",getParaToLong("id"));
		
		if(ValidateKit.isNullOrEmpty(types)){
			for(int i=0;i<apartmenttype.size();i++){
				System.out.println(apartmenttype.get(i).getLong("type_id")+" 已取消，执行删除操作！");
				apartmenttype.get(i).delete();
			}
		}else if(ValidateKit.isNullOrEmpty(apartmenttype)){
			for(int i=0;i<types.length;i++){
				System.out.println(types[i]+" 不存在，执行新增操作！");
				ApartmentType at=new ApartmentType().set("apartment_id",  getPara("id")).set("type_id", types[i]).set("level", "2");
				at.save();
			}
			
		}else{
			//需要处理该故障类型?，不存在则新增。
			for(int i=0;i<types.length;i++){
				boolean flag=false;
				for(int j=0;j<apartmenttype.size();j++){
					if(Integer.parseInt(types[i])==apartmenttype.get(j).getLong("type_id")){
						flag=true;
						break;
					}
				}
				if(flag){
					System.out.println(types[i]+" 存在，保留不删除！");
				}else if(!flag){
					System.out.println(types[i]+" 不存在，执行新增操作！");
					ApartmentType at=new ApartmentType().set("apartment_id",  getPara("id")).set("type_id", types[i]).set("level", "2");
					at.save();
				}
			}
			
			//需要保留该故障类型?，不保留则删除
			for(int i=0;i<apartmenttype.size();i++){
				boolean flag=false;
				for(int j=0;j<types.length;j++){
					if(apartmenttype.get(i).getLong("type_id")==Integer.parseInt(types[j])){
						flag=true;
						break;
					}
				}
				if(flag){
					System.out.println(apartmenttype.get(i).getLong("type_id")+" 未取消，保留不删除！");
				}else if(!flag){
					System.out.println(apartmenttype.get(i).getLong("type_id")+" 已取消，执行删除操作！");
					apartmenttype.get(i).delete();
				}
			}
			
		}
		redirect("/admin/task/rootTask");
		
	}

}
