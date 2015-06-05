package com.poicom.basic.kit;

import java.text.DecimalFormat;
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
		System.out.println(getUuidByJdk(false));
	}

}
