package com.poicom.common.resource;

import java.io.IOException;
import java.util.Map;

import cn.dreampie.PropertiesKit;
import freemarker.core.Environment;
import freemarker.log.Logger;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;

public class StaticTag extends DefaultTag {
	
	private static final Logger log = Logger.getLogger("StaticTag");
	
	private static String res_static = "";
	
	public StaticTag() {
        PropertiesKit.me().loadPropertyFile("application.properties");
        res_static = PropertiesKit.me().getProperty("resource.static", "");
    }

	@Override
	public void render(Environment env, Map params, TemplateDirectiveBody body)
			throws IOException, TemplateException {
		env.getOut().write(res_static);

	}

}
