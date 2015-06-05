package com.poicom.basic.freemarker.resource;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.*;

public abstract class DefaultTag implements TemplateDirectiveModel {

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,TemplateDirectiveBody body) throws TemplateException, IOException {
		verifyParameters(params);
		render(env, params, body);

	}
	
	@SuppressWarnings("rawtypes")
	public abstract void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException;
	
	
	@SuppressWarnings("rawtypes")
	protected String getParam(Map params, String name) {
        Object value = params.get(name);

        if (value instanceof SimpleScalar) {
            return ((SimpleScalar) value).getAsString();
        }

        return null;
    }
	
	@SuppressWarnings("rawtypes")
	protected void verifyParameters(Map params) throws TemplateModelException {
    }
	
	protected void renderBody(Environment env, TemplateDirectiveBody body) throws IOException, TemplateException {
        if (body != null) {
            body.render(env.getOut());
        }
    }

}
