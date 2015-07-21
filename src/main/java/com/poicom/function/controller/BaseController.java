package com.poicom.function.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;
import cn.dreampie.shiro.core.SubjectKit;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.poicom.basic.common.DictKeys;
import com.poicom.basic.common.SplitPage;
import com.poicom.basic.plugin.properties.PropertiesPlugin;
import com.poicom.function.model.Syslog;
import com.poicom.function.model.User;
/**
 * 
 * @author FireTercel 2015年6月2日 
 *
 */
public class BaseController extends Controller{
	
	@SuppressWarnings("unused")
	private static Logger logger=LoggerFactory.getLogger(BaseController.class);
	
	/**
	 * 全局变量
	 */
	protected Long id;						//主键
	protected SplitPage splitPage;		//分页封装
	protected List<?> list;					//公共list
	protected Syslog reqSysLog;		// 访问日志
	
	/**
	 * 请求/WEB-INF/下的视图文件
	 */
	public void toUrl() {
		String toUrl = getPara("toUrl");
		render(toUrl);
	}
	
	/**
	 * 获取当前请求国际化参数
	 * @return
	 */
	protected String getI18nPram() {
		return getAttr("localePram");
	}
	
	/**
	 * 获取项目请求根路径
	 * @return
	 */
	protected String getCxt() {
		return getAttr("ContextPath");
	}
	
	/**
	 * 获取当前用户id
	 * @return
	 */
	protected Long getCUserId(){
		return getAttr("cUserId");
	}
	
	/**
	 * 获取当前用户
	 * @return
	 */
	protected String getCUserName(){
		return getAttr("cUser");
	}
	
	protected User getCUser(){
		User user=SubjectKit.getUser();
		if(ValidateKit.isNullOrEmpty(user)){
			return null;
		}else{
			return user;
		}
	}
	
	/**
	 * 获取ParamMap
	 * @return
	 */
	protected Map<String, String> getParamMap(){
		return getAttr("paramMap");
	}

	/**
	 * 添加值到ParamMap
	 * @return
	 */
	protected void addToParamMap(String key, String value){
		Map<String, String> map = getAttr("paramMap");
		map.put(key, value);
	}
	
	/**
	 * 重写getPara，进行二次decode解码
	 */
	/*@Override
	public String getPara(String name) {
		String value = getRequest().getParameter(name);
		if(null != value && !value.isEmpty()){
			try {
				value = URLDecoder.decode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("decode异常：" + value + e.getMessage());
				e.printStackTrace();
			}
		}
		return value;
	}*/
	
	/**
	 * 获取checkbox值，数组
	 * @param name
	 * @return
	 */
	protected String[] getParas(String name) {
		return getRequest().getParameterValues(name);
	}
	
	/**
	 * @描述 Record 转换为Attr
	 * @param record
	 */
	public void setAttr(Record record){
		String[] column=record.getColumnNames();
		for(int i=0;i<column.length;i++){
			setAttr(column[i],record.get(column[i]));
		}
	}
	
	/**
	 * 效验Referer有效性
	 * @return
	 */
	protected boolean authReferer(){
		String referer = getRequest().getHeader("Referer");
		if(null != referer && !referer.trim().equals("")){
			referer = referer.toLowerCase();
			String domainStr = (String) PropertiesPlugin.getParamMapValue(DictKeys.config_domain_key);
			String [] domainArr = domainStr.split(",");
			for(String domain : domainArr){
				if(referer.indexOf(domain.trim()) != -1){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取查询参数
	 * 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 * @return
	 */
	protected Map<String,String> getQueryParam(){
		Map<String,String> queryParam = new HashMap<String,String>();
		Enumeration<String> paramNames = getParaNames();
		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement();
			String value = getPara(name);
			if (name.startsWith("_query") && !value.isEmpty()) {// 查询参数分拣
				String key = name.substring(7);
				if(null != value && !value.trim().equals("")){
					queryParam.put(key, value.trim());
				}
			}
		}
		
		return queryParam;
	}
	
	/**
	 * 设置默认排序
	 * @param colunm
	 * @param mode
	 */
	protected void defaultOrder(String colunm, String mode){
		if(null == splitPage.getOrderColunm() || splitPage.getOrderColunm().isEmpty()){
			splitPage.setOrderColunm(colunm);
			splitPage.setOrderMode(mode);
		}
	}
	
	/**
	 * 排序条件
	 * 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 * @return
	 */
	protected String getOrderColunm(){
		String orderColunm = getPara("orderColunm");
		return orderColunm;
	}
	
	/**
	 * 排序方式
	 * 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 * @return
	 */
	protected String getOrderMode(){
		String orderMode = getPara("orderMode");
		return orderMode;
	}
	
	
	public Syslog getReqSysLog() {
		return reqSysLog;
	}

	public void setReqSysLog(Syslog reqSysLog) {
		this.reqSysLog = reqSysLog;
	}
	
	public void setSplitPage(SplitPage splitPage) {
		this.splitPage = splitPage;
	}

}
