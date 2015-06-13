package com.poicom.basic.plugin.sqlxml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * 保存sql节点的id和value
 * @author FireTercel 2015年6月2日 
 *
 */
@XmlRootElement
public class SqlItem {

	@XmlAttribute
	String id;
	
	@XmlValue
	String value;
}
