package com.poicom.common.freemarker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.render.FreeMarkerRender;

/**
 * 实现视图耗时的计算
 * @author 唐东宇
 *
 */
public class MyFreeMarkerRender extends FreeMarkerRender {
	
	private static Logger logger=LoggerFactory.getLogger(MyFreeMarkerRender.class);

	public static final String viewTimeKey="viewTime";
	
	public MyFreeMarkerRender(String view) {
		super(view);
	}
	
	public void render(){
		long start = System.currentTimeMillis();
		logger.debug("MyFreeMarkerRender render start time = "+ start);
		
		super.render();
		
		long end=System.currentTimeMillis();
		long viewTime = end - start;
		
		logger.debug("MyFreeMarkerRender render end time = "+ end +" , viewTime = "+ viewTime);
		
		request.setAttribute(MyFreeMarkerRender.viewTimeKey, viewTime);
		
	}

}
