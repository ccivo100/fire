package com.poicom.basic.thread;

import com.jfinal.log.Logger;

public class ThreadParamInit extends Thread {
	
	private static Logger log = Logger.getLogger(ThreadParamInit.class);
	
	public static String cacheStart_role = "role_";
	public static String cacheStart_permission = "permission_";
	public static String cacheStart_user = "user_";
	public static String cacheStart_branch = "branch_";
	public static String cacheStart_apartment = "apartment_";
	public static String cacheStart_position = "position_";
	public static String cacheStart_operator = "operator_";
	public static String cacheStart_dict = "dict_";
	public static String cacheStart_dict_child = "dict_child_";
	
	@Override
	public void run() {
		cacheAll();
	}
	
	public static synchronized void cacheAll(){
		
	}

}
