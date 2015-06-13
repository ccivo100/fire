package com.poicom.basic.plugin.sqlxml;

import com.jfinal.plugin.IPlugin;

/**
 * 加载SQL文件
 * @author FireTercel 2015年6月2日 
 *
 */
public class SqlInXmlPlugin implements IPlugin{
	
	private String[] paths = null;
	
	public SqlInXmlPlugin(){
		
	}
	public SqlInXmlPlugin(String... paths){
		this.paths=paths;
	}

	@Override
	public boolean start() {
		if(paths !=null){
			SqlXmlKit.init(paths);
		}else
			SqlXmlKit.init();
		return true;
	}

	@Override
	public boolean stop() {
		SqlXmlKit.destroy();
		return false;
	}

}
