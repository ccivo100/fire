package com.poicom.function.job;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poicom.function.app.model.Order;

public class AlertJob implements Job{
	
	protected Logger logger=LoggerFactory.getLogger(getClass());

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		// TODO Auto-generated method stub
		Map data=jobExecutionContext.getJobDetail().getJobDataMap();
		System.out.println("hi,"+data.get("name")+"," + DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		
		List<Order> orders =Order.dao.findAll();
		for(Order order:orders){
			
		}
		
	}

}
