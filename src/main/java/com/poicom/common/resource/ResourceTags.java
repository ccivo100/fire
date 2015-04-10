package com.poicom.common.resource;

import freemarker.template.SimpleHash;

public class ResourceTags extends SimpleHash {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2074900314378139571L;

	public ResourceTags(){
		put("static", new StaticTag());
	}

}
