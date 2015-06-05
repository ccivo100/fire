package com.poicom.basic.plugin.sqlxml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

/**
 * SqlGroup保存sql节点信息
 * @author FireTercel 2015年6月2日 
 *
 */
@XmlRootElement
public class SqlGroup {
	@XmlAttribute
	String name;
	@XmlElement(name = "sql")
	List<SqlItem> sqlItems = Lists.newArrayList();
	
	void addSqlgroup(SqlItem sqlGroup){
		sqlItems.add(sqlGroup);
	}

}
