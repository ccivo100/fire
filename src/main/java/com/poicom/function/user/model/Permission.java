package com.poicom.function.user.model;

import java.util.List;

import cn.dreampie.sqlinxml.SqlKit;
import cn.dreampie.tablebind.TableBind;
import cn.dreampie.tree.TreeNode;
import cn.dreampie.web.model.Model;

/**
 * 
 * @author DONGYU
 *
 */
@TableBind(tableName="sec_permission")
public class Permission extends Model<Permission> implements TreeNode<Permission>{
	public static Permission dao=new Permission();

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return this.getLong("id");
	}

	@Override
	public long getParentId() {
		// TODO Auto-generated method stub
		return this.getLong("pid");
	}

	@Override
	public List<Permission> getChildren() {
		// TODO Auto-generated method stub
		return this.get("children");
	}

	@Override
	public void setChildren(List<Permission> children) {
		// TODO Auto-generated method stub
		this.put("children", children);
	}
	
	/**
	 * sql拼接
	 * getSelectSql()方法获得“select modle.*”;
	 * SqlKit.sql()方法获得配置文件中“permission.findRoleByFrom”这段sql语句;
	 * blank属性防止意外加上一个空格;
	 * getWhere(where)，参数where为条件，拼接在“ WHERE ”之后
	 * param参数为占位符?参数
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<Permission> findByRole(String where,Object... paras){
		return find(getSelectSql()+SqlKit.sql("permission.findRoleByFrom") + blank + getWhere(where), paras);
	}

}
