package com.poicom.basic.plugin.sqlxml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

/**
 * SqlRoot保存SqlGroup节点信息
 * @author FireTercel 2015年6月2日 
 *
 */
@XmlRootElement
public class SqlRoot {

	@XmlElement(name = "sqlGroup")
	List<SqlGroup> sqlGroups = Lists.newArrayList();
	
	void addSqlRoot(SqlGroup sqlGroup){
		sqlGroups.add(sqlGroup);
	}
}
