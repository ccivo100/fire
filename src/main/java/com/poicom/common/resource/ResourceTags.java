package com.poicom.common.resource;

import com.poicom.common.freemarker.LoginExceptionTag;

import freemarker.template.SimpleHash;

public class ResourceTags extends SimpleHash {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2074900314378139571L;

	public ResourceTags(){
		put("static", new StaticTag());
		put("loginException",new LoginExceptionTag());
	}

}
