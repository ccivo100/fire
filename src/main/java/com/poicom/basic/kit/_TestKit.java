package com.poicom.basic.kit;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

import org.junit.Test;

import cn.dreampie.ValidateKit;

public class _TestKit {
	
	/**
	 * 测试，获得类名，将第一个字母改为小写字母
	 */
	
	public void test(){
		Class<? extends _TestKit> clazz=getClass();
		String modelName=clazz.getSimpleName();
		byte[] items=clazz.getSimpleName().getBytes();
		items[0]=(byte)((char)items[0]+('a'-'A'));
		modelName=new String(items);
		System.out.println(modelName+" "+('a'-'A'));
	}
	
	public void t_resources(){
		Class clazz=this.getClass();
		URLClassLoader loader = (URLClassLoader)clazz.getClassLoader();
		Enumeration<URL> urls;
		try {
			urls=loader.getResources("WebKit.java");
			System.out.println(this.getClass().getResource("/"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void t_isEmail(){
		String email="dantechan@poicom.net";
		if(ValidateKit.isEmail(email)){
			System.out.println("true");
		}else
			System.out.println("false");
	}
	
	@Test
	public void t_class(){
		Method[] methods = _TestKit.class.getMethods();
		for (Method m : methods) {
			if (m.getParameterTypes().length == 0)
				System.out.println(m.getName());
			else{
				System.out.println("===============");
				System.out.println(m.getName());
				for(Class clazz:m.getParameterTypes()){
					System.out.println(clazz.getName());
				}
			}
		}
	}

}
