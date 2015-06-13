package com.poicom.function.service;

import com.jfinal.log.Logger;
import com.poicom.basic.common.DictKeys;
import com.poicom.basic.common.SplitPage;
import com.poicom.basic.kit.WebKit;
import com.poicom.function.model.Permission;
import com.poicom.function.model.Role;

public class RoleService extends BaseService {
	
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(RoleService.class);
	
	public static final RoleService service = new RoleService();
	
	/**
	 * 保存
	 * @param role
	 * @return
	 */
	public Long save(Role role){
		role.set("name", WebKit.delHTMLTag(role.getStr("name"))).set("value", WebKit.delHTMLTag(role.getStr("value")));
		role.save();
		return role.getPKValue();
	}
	
	/**
	 * 更新、设置不可用
	 * @param role
	 */
	public void update(Role role){
		role.set("name", WebKit.delHTMLTag(role.getStr("name"))).set("value", WebKit.delHTMLTag(role.getStr("value")));
		role.update();
		Role.dao.cacheAdd(role.getPKValue());
	}
	
	
	/**
	 * 删除
	 * @param roleid
	 */
	public void drop(Long roleid){
		Role.dao.cacheRemove(roleid);
		Role.dao.deleteById(roleid);
	}
	
	/**
	 * 设置角色权限
	 * @param permission
	 * @return
	 */
	public Role addPermission(Permission permission){
		return Role.dao.addPermission(permission);
	}
	
	public void list(SplitPage splitPage){
		splitPageBase(DictKeys.db_dataSource_main, splitPage,Role.dao.getSelectSql(),"role.splitPage");
	}

}
