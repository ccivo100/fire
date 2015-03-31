package com.poicom.function.job;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poicom.function.app.model.Order;
import com.poicom.function.user.model.User;

public class AlertJob implements Job{
	
	protected Logger logger=LoggerFactory.getLogger(getClass());

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		// TODO Auto-generated method stub
		Map data=jobExecutionContext.getJobDetail().getJobDataMap();
		System.out.println("hi,"+data.get("name")+"," + DateTime.now().toString("yyyy-MM-dd HH:mm:Ss"));
		
		List<Order> orders =Order.dao.findOrderByStatus(1);
		Map<Long,User> userMap=new HashMap<Long,User>();
		DateTimeFormatter format = DateTimeFormat .forPattern("yyyy-MM-dd HH:mm:ss.SS");  
		DateTime now=DateTime.now();
		for(Order order:orders){
			DateTime offer_at=DateTime.parse(order.get("offer_at").toString(),format); 
			int t=dateBetween(offer_at,now);
			if(t>24){
				doAlert();
			}
		}
		
	}
	/**
	 * @描述 执行提醒操作，并且将order的status设置为2
	 */
	public void doAlert(){
		
	}
	
	/**
	 * @描述 判断申报时间和现在时间差。
	 * @param offer_at
	 * @param now
	 * @return
	 */
	public int dateBetween(DateTime offer_at,DateTime now){
		
		/*System.out.print("两个时间相差：");
        System.out.print(Days.daysBetween(offer_at, now).getDays() + " 天, ");
        System.out.print(Hours.hoursBetween(offer_at, now).getHours() % 24
                + " 小时, ");
        System.out.print(Minutes.minutesBetween(offer_at, now).getMinutes() % 60
                + " 分钟, ");
        System.out.println(Seconds.secondsBetween(offer_at, now).getSeconds() % 60
                + " 秒.");*/
        int time=Days.daysBetween(offer_at, now).getDays()*24+Hours.hoursBetween(offer_at, now).getHours() % 24;
		return time;
	}

}
