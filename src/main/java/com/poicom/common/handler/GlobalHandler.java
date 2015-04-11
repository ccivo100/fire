package com.poicom.common.handler;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.handler.Handler;
import com.poicom.common.freemarker.MyFreeMarkerRender;
import com.poicom.common.kit.DateKit;
import com.poicom.common.kit.WebKit;
import com.poicom.common.thread.ThreadSysLog;
import com.poicom.function.system.model.Syslog;


/**
 * 全局Handler，设置一些通用功能
 * @author poicom7
 *
 */
public class GlobalHandler extends Handler{
	
	private static Logger logger=LoggerFactory.getLogger(GlobalHandler.class);
	
	public static final String reqSysLogKey="reqSysLog";

	@Override
	public void handle(String target, HttpServletRequest request,	HttpServletResponse response, boolean[] isHandled) {
		logger.info("初始化访问系统日志功能");
		Syslog reqSysLog=getSysLog(request);
		
		//开始时间
		long starttime=DateKit.getDateByTime();
		reqSysLog.set("startdate", DateKit.getSqlTimestamp(starttime));
		request.setAttribute(reqSysLogKey, reqSysLog);
		
		logger.info("设置 web 路径");
		String cxt=WebKit.getContextPath(request);
		request.setAttribute("ContextPath", cxt);
		
		logger.debug("request cookie 处理");
		Map<String,Cookie> cookieMap=WebKit.readCookieMap(request);
		request.setAttribute("cookieMap", cookieMap);
		
		logger.debug("request param 请求参数处理");
		request.setAttribute("paramMap", WebKit.getParamMap(request));
		
		logger.info("设置Header");
		request.setAttribute("decorator", "none");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		nextHandler.handle(target, request, response, isHandled);
		
		logger.info("请求处理完毕，计算耗时");
		
		//结束时间
		long endtime=DateKit.getDateByTime();
		reqSysLog.set("enddate", DateKit.getSqlTimestamp(endtime));
		
		//总耗时
		long haoshi =endtime-starttime;
		reqSysLog.set("haoshi", haoshi);
		
		//视图耗时
		long viewtime=0;
		if(null!=request.getAttribute(MyFreeMarkerRender.viewTimeKey)){
			viewtime=(long) request.getAttribute(MyFreeMarkerRender.viewTimeKey);
		}
		reqSysLog.set("viewhaoshi", viewtime);
		
		//action耗时
		reqSysLog.set("actionhaoshi", haoshi-viewtime);
		
		logger.info("日志添加到入库队列");
		ThreadSysLog.add(reqSysLog);
	}
	
	
	/**
	 * 创建日志对象，并初始化一些属性值
	 * @param request
	 * @return
	 */
	public Syslog getSysLog(HttpServletRequest request){
		String requestPath=WebKit.getRequestURIWithParam(request);
		String ip=WebKit.getIpAddr(request);
		String referer = request.getHeader("Referer"); 
		String userAgent = request.getHeader("User-Agent");
		String cookie = request.getHeader("Cookie");
		String method = request.getMethod();
		String xRequestedWith = request.getHeader("X-Requested-With");
		String host = request.getHeader("Host");
		String acceptLanguage = request.getHeader("Accept-Language");
		String acceptEncoding = request.getHeader("Accept-Encoding");
		String accept = request.getHeader("Accept");
		String connection = request.getHeader("Connection");
		
		Syslog reqSysLog=new Syslog();
		
		reqSysLog.set("ips", ip);
		reqSysLog.set("requestpath", requestPath);
		reqSysLog.set("referer", referer);
		reqSysLog.set("useragent", userAgent);
		reqSysLog.set("cookie", cookie);
		reqSysLog.set("method", method);
		reqSysLog.set("xrequestedwith", xRequestedWith);
		reqSysLog.set("host", host);
		reqSysLog.set("acceptlanguage", acceptLanguage);
		reqSysLog.set("acceptencoding", acceptEncoding);
		reqSysLog.set("accept", accept);
		reqSysLog.set("connection", connection);
		
		return reqSysLog;
		
	}

}
