package com.poicom.common.freemarker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;

/**
 * 实现视图耗时计算
 * @author poicom7
 *
 */
public class FreeMarkerRenderFactory implements IMainRenderFactory{

	private static Logger logger=LoggerFactory.getLogger(FreeMarkerRenderFactory.class);
	
	@Override
	public Render getRender(String view) {
		logger.debug("FreeMarkerRenderFactory start ");
		FreeMarkerRender render =new MyFreeMarkerRender(view);
		logger.debug("FreeMarkerRenderFactory end ");
		return render;
	}

	@Override
	public String getViewExtension() {
		// TODO Auto-generated method stub
		return ".html";
	}

}
