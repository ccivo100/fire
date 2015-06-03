package com.poicom.common.kit;

import java.util.regex.Pattern;

import cn.dreampie.ValidateKit;

public class ValiKit extends ValidateKit {
	
	  /**
	   * 电话号码 包括移动电话和座机
	   *
	   * @param value value
	   * @return boolean
	   */
	  public static  boolean isPhone(String value) {
	    //String telcheck = "^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$";
	    String mobilecheck = "^(13[0-9]|14[0-9]|15[0|1|2|3|5|6|7|8|9]|17[6|7|8]|18[0-9])\\d{8}$";

	    if (match(mobilecheck, Pattern.CASE_INSENSITIVE, value)) {
	      return true;
	    } else
	      return false;
	  }

}
