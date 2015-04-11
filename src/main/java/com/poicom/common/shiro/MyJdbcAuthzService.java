package com.poicom.common.shiro;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.poicom.function.system.model.Permission;
import com.poicom.function.system.model.Role;

import cn.dreampie.shiro.core.JdbcAuthzService;
import cn.dreampie.shiro.core.handler.AuthzHandler;
import cn.dreampie.shiro.core.handler.JdbcPermissionAuthzHandler;

/**
 * 实现数据库权限的初始化加载
 * 实现的接口是Dreampie的JdbcAuthzService
 * 只有一个方法getJdbcAuthz()，return一个Map<String,AuthzHandler>
 * @author Administrator
 *
 */
public class MyJdbcAuthzService implements JdbcAuthzService {

	/**
	 * AuthzHandler是一个访问控制处理器接口
	 */
	@Override
	public Map<String, AuthzHandler> getJdbcAuthz() {
		//加载数据库的url配置，并按长度倒序排列
		Map<String, AuthzHandler> authzJdbcMaps=Collections.synchronizedMap(
				new TreeMap<String,AuthzHandler>(
				new Comparator<String>(){

					//对比URL长度
					@Override
					public int compare(String url1, String url2) {
						int result=url2.length()-url1.length();
						if(result==0){
							return url1.compareTo(url2);
						}
						return result;
					}
					
				}));
		//遍历角色
		//roles获取数据库中所有role
		List<Role> roles=Role.dao.findAll();
		List<Permission> permissions=null;
		for (Role role : roles) {
			//角色可用
			if(role.getDate("deleted_at")==null){
				//获得这个角色的所有权限
				permissions=Permission.dao.findByRole("", role.get("id"));
				for (Permission permission : permissions) {
					//权限可用
					if(permission.get("deleted_at")==null){
						//url的值非NULL、非""
						if(permission.getStr("url")!=null&&!permission.getStr("url").isEmpty()){
							//类JdbcPermissionAuthzHandler是Dreampie的jfinal-shiro自带的
							//主要代码为访问控制检查(subject.checkPermission(jdbcPermission);)
							authzJdbcMaps.put(permission.getStr("url"), new JdbcPermissionAuthzHandler(permission.getStr("value")));
						}
					}
				}
				
			}
		}
		return authzJdbcMaps;
	}

}
