package com.poicom.basic.kit;

import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HSRequestKit {
	private final static Logger logger= LoggerFactory.getLogger(HSRequestKit.class);
	private static HttpServletRequest request;
	
	public HSRequestKit(){}
	//拦截器中调用该构造器
	public HSRequestKit(HttpServletRequest req){
		request=req;
		//初始化方法，把需要打印的内容添加这个方法中
		init();
	}
	public static void init(){
		//打印客户机信息
		printHttpInfo();
		//打印客户机请求头信息
		printHttpHeadInfo();
		
		printHttpReqParams();
	}
	public static void printHttpInfo(){
		String uri=request.getRequestURI();
		String rad=request.getRemoteAddr();
		String rh=request.getRemoteHost();
		String ru=request.getRemoteUser();
		int rp=request.getRemotePort();
		String cp=request.getContextPath();
		String la=request.getLocalAddr();
		String ce=request.getCharacterEncoding();
		String gm=request.getMethod();
		String qs=request.getQueryString();
		
		logger.info("    ======   This will get 客户机信息  start ====  ");
		
		logger.info("URI： "+uri);
		logger.info("RemoteAddr： "+rad);
		logger.info("RemoteHost： "+rh);
		logger.info("RemoteUser： "+ru);
		logger.info("RemotePort： "+rp);
		logger.info("ContextPath： "+cp);
		logger.info("LocalAddr： "+la);
		logger.info("CharEncoding： "+ce);
		logger.info("Method： "+gm);
		logger.info("QueryString： "+qs);
		logger.info("    ======   This will get 客户机信息  end ====  ");
	}
	
	public static void printHttpHeadInfo(){
		logger.info("    ======   This will get 客户机请求头信息  start ====  ");
		logger.info(""+request.getHeader("method"));
		Enumeration<String> e=request.getHeaderNames();
		while(e.hasMoreElements()){
			String name=e.nextElement();
			String value=request.getHeader(name);
			logger.info(name+" ： "+value);
		}
		logger.info("    ======   This will get 客户机请求头信息  end ====  ");
	}
	
	public static void printHttpReqParams(){
		logger.info("    ======   This will print parameterMap ====  ");
		Map<String, String[]> parasMap = request.getParameterMap();
		if (parasMap==null) {
			logger.info("parameterMap is null");
		}else {
			for (Entry<String,String[]> e : parasMap.entrySet()) {
				String paraKey=e.getKey();
				logger.info(paraKey);
				
			}
		}
		
	}
	

}
