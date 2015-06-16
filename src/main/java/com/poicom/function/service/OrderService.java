package com.poicom.function.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.dreampie.shiro.core.SubjectKit;

import com.jfinal.log.Logger;
import com.poicom.function.model.Apartment;
import com.poicom.function.model.Etype;
import com.poicom.function.model.User;
import com.poicom.function.model.UserInfo;
import com.poicom.function.model.UserOrder;

public class OrderService extends BaseService {
	
	private static Logger log = Logger.getLogger(OrderService.class);
	
	public static OrderService service = new OrderService();
	
	/**
	 * 根据工单故障类型id，查其父类pid。排除已指派部门。
	 * @param typeid 工单故障子类型id
	 * @return 该工单未指派根部门list
	 */
	public List<Apartment> apartmentByTypeExcApart(Long orderid,Long typeid){
		Set<Long> apidSet=new HashSet<Long>();
		
		List<User> userList = UserOrder.dao.getUserList(orderid);//根据工单id，查询工单拥有者
		for(User user:userList){
			UserInfo userinfo = UserInfo.dao.get("user_id",user.get("id"));//每个拥有者的详细信息
			Long apartment_id = userinfo.getLong("apartment_id");//拥有者的部门id（二级部门）
			Apartment apartment = Apartment.dao.findById(apartment_id);//二级部门信息
			Long apartment_pid=apartment.getLong("pid");//父级部门
			apidSet.add(apartment_pid);
		}
		
		Long typepid = Etype.dao.findById(typeid).getLong("pid");//根据细类id获得父类id
		Long[] apids=apidSet.toArray(new Long[apidSet.size()]);
		Long[] l1={typepid};
		int lsize1=l1.length;
		int lsize2=apids.length;
		
		l1= Arrays.copyOf(l1,lsize1+ lsize2);//扩容
		System.arraycopy(apids, 0, l1, lsize1,lsize2 );//将第二个数组与第一个数组合并
		
		List<Apartment> apartmentList=Apartment.dao.getApartmentsListExcept(l1);
		return apartmentList;
	}
	
	public Map<Long,Apartment> getDealApartmentsByOrderid(Long orderid){
		List<User> userList = UserOrder.dao.getUserList(orderid);//根据工单id，查询工单拥有者
		Map<Long,Apartment> papartmentMap=new HashMap<Long,Apartment>();//保存父级部门。
		for(User user:userList){
			UserInfo userinfo = UserInfo.dao.get("user_id",user.get("id"));//每个拥有者的详细信息
			Long apartment_id = userinfo.getLong("apartment_id");//拥有者的部门id（二级部门）
			Apartment apartment = Apartment.dao.findById(apartment_id);//二级部门信息
			Long papartment_id=apartment.getLong("pid");//父级部门id
			Apartment papartment;
			//在获得父级部门后，添加入
			if(papartmentMap.get(papartment_id)==null){
				papartment= Apartment.dao.findById(papartment_id);//父级部门信息
				Map<Long,Apartment> childList;
				if(papartment.getChild()==null){
					childList=new HashMap<Long,Apartment>();
				}else{
					childList=papartment.getChild();
				}
				childList.put(apartment_id,apartment);
				papartment.setChild(childList);
				papartmentMap.put(papartment_id, papartment);
			}else{
				papartment =papartmentMap.get(papartment_id);
				Map<Long,Apartment> childList;
				if(papartment.getChild()==null){
					childList=new HashMap<Long,Apartment>();
				}else{
					childList=papartment.getChild();
				}
				childList.put(apartment_id,apartment);
				papartment.setChild(childList);
			}
		}
		return papartmentMap;
	}
	
	public Map<Long,Apartment> getDealCApartmentsByOrderid(Long orderid){
		List<User> userList = UserOrder.dao.getUserList(orderid);//根据工单id，查询工单拥有者
		Map<Long,Apartment> papartmentMap=new HashMap<Long,Apartment>();//保存父级部门。
		for(User user:userList){
			UserInfo userinfo = UserInfo.dao.get("user_id",user.get("id"));//每个拥有者的详细信息
			Long apartment_id = userinfo.getLong("apartment_id");//拥有者的部门id（二级部门）
			Apartment apartment = Apartment.dao.findById(apartment_id);//二级部门信息
			papartmentMap.put(apartment_id, apartment);
		}
		return papartmentMap;
	}

}
