package com.poicom.common.quartz.job;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

import com.poicom.common.quartz.QuartzKey;
import com.poicom.common.quartz.QuartzKit;

/**
 * 
 * @author wangrenhui on 14/11/29.
 * 
 */
public class QuartzOnceJob extends QuartzJob {

	private Date startTime;

	public QuartzOnceJob(QuartzKey quartzKey, Date startTime,Class<? extends Job> jobClass) {
		this.quartzKey = quartzKey;
		this.startTime = startTime;
		this.jobClass = jobClass;
		this.state = JobState.INITED;
	}

	@Override
	public void start(boolean force) {
		QuartzJob quartzJob=QuartzKit.getJob(quartzKey);
		if(null!=quartzJob){
			if(force){
				quartzJob.stop();
			}else{
				return;
			}
		}
		long id = quartzKey.getId();
	    String name = quartzKey.getName();
	    String group = quartzKey.getGroup();
	    SchedulerFactory factory = QuartzKit.getSchedulerFactory();
	    
	    try{
	    	if (factory != null) {
	            Scheduler scheduler = factory.getScheduler();
	            JobDetail job = newJob(jobClass)
	                    .withIdentity(JOB_MARK + SEPARATOR + name + SEPARATOR + id, GROUP_MARK + SEPARATOR + group + SEPARATOR + id)
	                    .requestRecovery()
	                    .build();
	            Map jobMap = job.getJobDataMap();
	            jobMap.put(group + SEPARATOR + name, id);
	            //添加参数
	            if (params != null && params.size() > 0)
	              jobMap.putAll(params);
	            
	            // 定时执行
	            Trigger trigger = newTrigger()
	                .withIdentity(TRIGGER_MARK + SEPARATOR + name + SEPARATOR + id, GROUP_MARK + SEPARATOR + group + SEPARATOR + id)
	                .startAt(this.startTime)
	                .build();
	            
	            this.scheduleTime = scheduler.scheduleJob(job, trigger);
	            scheduler.start();
	            this.state=JobState.STARTED;
	            QuartzKit.addQuartzJob(this);
	            
	    	}
	    }catch(Exception e){
	    	throw new RuntimeException("Can't start once job.", e);
	    }
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
	    this.startTime = startTime;
	}
    
    

}
