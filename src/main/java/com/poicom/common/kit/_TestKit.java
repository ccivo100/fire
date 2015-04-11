package com.poicom.common.kit;

import org.junit.Test;

public class _TestKit {
	
	/**
	 * 测试，获得类名，将第一个字母改为小写字母
	 */
	@Test
	public void test(){
		Class<? extends _TestKit> clazz=getClass();
		String modelName=clazz.getSimpleName();
		byte[] items=clazz.getSimpleName().getBytes();
		items[0]=(byte)((char)items[0]+('a'-'A'));
		modelName=new String(items);
		System.out.println(modelName+" "+('a'-'A'));
	}

}
