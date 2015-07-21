package com.poicom.basic.freemarker.tag;

import java.io.IOException;

import com.poicom.basic.freemarker.common.base.BaseDirective;
import com.poicom.basic.freemarker.common.handler.DirectiveHandler;

import freemarker.template.TemplateException;

public abstract class SecureTag extends BaseDirective{

	@Override
	public void execute(DirectiveHandler handler) throws TemplateException, IOException {
		
		handler.render();
	}
	
	

}
