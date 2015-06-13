package com.poicom.function.service;

import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.poicom.basic.common.SplitPage;
import com.poicom.basic.plugin.sqlxml.SqlXmlKit;

public class BaseService {
	
	private static Logger log = LoggerFactory.getLogger(BaseService.class);
	
	/**
	 * 获取SQL，固定SQL
	 * @param sqlId
	 * @return
	 */
	protected String getSql(String sqlId){
		return SqlXmlKit.sql(sqlId);
	}
	
	/**
	 * 获取SQL，动态SQL
	 * @param sqlId
	 * @param param
	 * @return
	 */
	protected String getSql(String sqlId,Map<String,Object> param){
		return SqlXmlKit.sql(sqlId, param);
	}
	
	/**
	 * 获取SQL，动态SQL
	 * @param sqlId
	 * @param param 查询参数
     * @param list 用于接收预处理的值
	 * @return
	 */
	protected String getSql(String sqlId,Map<String,String> param, LinkedList<Object> list){
		return SqlXmlKit.sql(sqlId, param, list);
	}
	
	/**
	 * 把11,22,33...转成'11','22','33'...
	 * @param ids
	 * @return
	 */
	protected String toSql(String ids){
		if(null == ids || ids.trim().isEmpty()){
			return null;
		}
		
		String[] idsArr = ids.split(",");
		StringBuilder sqlSb = new StringBuilder();
		int length = idsArr.length;
		for (int i = 0, size = length -1; i < size; i++) {
			sqlSb.append(" '").append(idsArr[i]).append("', ");
		}
		if(length != 0){
			sqlSb.append(" '").append(idsArr[length-1]).append("' ");
		}
		
		return sqlSb.toString();
	}
	
	
	/**
	 * 把数组转成'11','22','33'...
	 * @param ids
	 * @return
	 */
	protected String toSql(String[] idsArr){
		if(idsArr == null || idsArr.length == 0){
			return null;
		}
		
		StringBuilder sqlSb = new StringBuilder();
		int length = idsArr.length;
		for (int i = 0, size = length -1; i < size; i++) {
			sqlSb.append(" '").append(idsArr[i]).append("', ");
		}
		if(length != 0){
			sqlSb.append(" '").append(idsArr[length-1]).append("' ");
		}
		
		return sqlSb.toString();
	}
	
	/**
	 * 分页，保存结果于 SplitPage对象
	 * @param dataSource 数据源
	 * @param splitPage
	 * @param select
	 * @param sqlId
	 */
	protected void splitPageBase(String dataSource, SplitPage splitPage, String select, String sqlId){
		// 接收返回值对象
		StringBuilder formSqlSb = new StringBuilder();
		LinkedList<Object> paramValue = new LinkedList<Object>();
		
		// 调用生成from sql，并构造paramValue
		String sql = SqlXmlKit.sql(sqlId, splitPage.getQueryParam(), paramValue);
		formSqlSb.append(sql);
		
		// 行级：过滤
		rowFilter(formSqlSb);
		
		// 排序
		String orderColunm = splitPage.getOrderColunm();
		String orderMode = splitPage.getOrderMode();
		if(null != orderColunm && !orderColunm.isEmpty() && null != orderMode && !orderMode.isEmpty()){
			formSqlSb.append(" order by ").append(orderColunm).append(" ").append(orderMode);
		}
		
		String formSql = formSqlSb.toString();

		Page<?> page = Db.use(dataSource).paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select, formSql, paramValue.toArray());
		splitPage.setTotalPage(page.getTotalPage());
		splitPage.setTotalRow(page.getTotalRow());
		splitPage.setList(page.getList());
		splitPage.compute();
	}
	
	/**
	 * 行级：过滤
	 * @return
	 */
	protected void rowFilter(StringBuilder formSqlSb){
		
	}

}
