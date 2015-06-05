package com.poicom.basic.freemarker.resource;

import com.poicom.basic.freemarker.tag.LoginExceptionTag;

import freemarker.template.SimpleHash;

public class ResourceTags extends SimpleHash {

	private static final long serialVersionUID = 2074900314378139571L;

	public ResourceTags(){
		put("static", new StaticTag());
		put("loginException",new LoginExceptionTag());
	}

}
