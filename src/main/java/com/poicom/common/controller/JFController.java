package com.poicom.common.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.routebind.ControllerKey;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.poicom.function.system.model.Syslog;

@ControllerKey(value="/jf")
public class JFController extends Controller{
	
	private static Logger logger=LoggerFactory.getLogger(JFController.class);
	
	/**
	 * 全局变量
	 */
	protected Syslog reqSysLog;		// 访问日志
	
	/**
	 * 请求/WEB-INF/下的视图文件
	 */
	public void toUrl() {
		String toUrl = getPara("toUrl");
		render(toUrl);
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
	protected String getCUserId(){
		return getAttr("cUserId");
	}
	
	/**
	 * 获取当前用户
	 * @return
	 */
	protected String getCUser(){
		return getAttr("cUser");
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
	@Override
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
	}
	
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
	
	public Syslog getReqSysLog() {
		return reqSysLog;
	}

	public void setReqSysLog(Syslog reqSysLog) {
		this.reqSysLog = reqSysLog;
	}

}
