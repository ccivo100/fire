package com.poicom.basic.freemarker.common.base;

import java.io.IOException;
import java.util.Map;

import com.poicom.basic.freemarker.common.handler.DirectiveHandler;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public abstract class BaseDirective implements TemplateDirectiveModel {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, @SuppressWarnings("rawtypes")Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		
		execute(new DirectiveHandler(env, params,loopVars, body));
	}
	
	public abstract void execute(DirectiveHandler handler) throws TemplateException, IOException;

}
