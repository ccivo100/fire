package com.poicom.common.kit;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateKit {
	
	protected Logger logger=LoggerFactory.getLogger(getClass());
	
	public static DateTimeFormatter format;
	
	public static int dateBetween(String offer,DateTime now){
		format = DateTimeFormat .forPattern("yyyy-MM-dd HH:mm:ss.SS");
		DateTime offer_at=DateTime.parse(offer,format);
		System.out.print("两个时间相差：");
		int time=Days.daysBetween(offer_at, now).getDays()*24+Hours.hoursBetween(offer_at, now).getHours() % 24;
        System.out.println("一共 "+time+" 小时 ");
        return time;
	}

}
