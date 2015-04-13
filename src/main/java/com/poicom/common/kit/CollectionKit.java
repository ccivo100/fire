package com.poicom.common.kit;

import java.util.HashSet;
import java.util.List;

import com.jfinal.core.Controller;

/**
 * 集合工具类
 * @author poicom7
 *
 */
public class CollectionKit {
	
	/**
	 * 去掉List里面重复项。
	 * @param list
	 * @return
	 */
	public static List<Class<? extends Controller>> removeDuplicate(List<Class<? extends Controller>> list){
		HashSet<Class<? extends Controller>> h = new HashSet<Class<? extends Controller>>(list);
		list.clear();
		list.addAll(h);
		return list;
	}
}
