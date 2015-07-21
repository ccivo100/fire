package com.poicom.basic.kit;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.Test;

public class UtilsKit {
	
private static Logger log= Logger.getLogger(UtilsKit.class);
	
	/**
	 * double精度调整
	 * @param doubleValue 需要调整的值123.454
	 * @param format 目标样式".##"
	 * @return
	 */
	public static String decimalFormatToString(double doubleValue,String format){
		DecimalFormat formatter=new DecimalFormat(format);
		String formatValue = formatter.format(doubleValue);
		return formatValue;
	}
	
	/**
	 * 获取UUID by jdk
	 * @param is32bit
	 * @return
	 */
	public static String getUuidByJdk(boolean is32bit){
		String uuid=UUID.randomUUID().toString();
		if(is32bit){
			return uuid.toString().replace("-", "");
		}
		return uuid;
	}
	
	@Test
	public void test(){
		Set<String> set = new HashSet<String>();
		long start = System.currentTimeMillis();
		for(int i=0;i<100000;i++){
			String uuid = getUuidByJdk(false);
			set.add(uuid);
			log.info(uuid);	
		}
		long end = System.currentTimeMillis();
		
		log.info(" Time is:"+(end-start)+" ms. \n Set's size is:"+set.size());
		
	}

}
