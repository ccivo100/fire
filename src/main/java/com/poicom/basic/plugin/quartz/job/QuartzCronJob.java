package com.poicom.basic.plugin.quartz.job;

import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;

import com.poicom.basic.plugin.quartz.QuartzKey;
import com.poicom.basic.plugin.quartz.QuartzKit;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzCronJob extends QuartzJob {

	private String cron;

	public QuartzCronJob(QuartzKey quartzKey, String cron,
			Class<? extends Job> jobClass) {
		this.quartzKey = quartzKey;
		this.state = JobState.INITED;
		this.cron = cron;
		this.jobClass = jobClass;
	}

	@Override
	public void start(boolean force) {
		QuartzJob quartzJob = QuartzKit.getJob(quartzKey);
		if (null != quartzJob) {
			if (force) {
				quartzJob.stop();
			} else {
				return;
			}
		}

		long id = quartzKey.getId();
		String name = quartzKey.getName();
		String group = quartzKey.getGroup();
		SchedulerFactory factory = QuartzKit.getSchedulerFactory();

		try {
			if (null != factory) {
				Scheduler scheduler = factory.getScheduler();
				JobDetail job = newJob(jobClass)
						.withIdentity(
								JOB_MARK + SEPARATOR + name + SEPARATOR + id,
								GROUP_MARK + SEPARATOR + group + SEPARATOR + id)
						.requestRecovery().build();
				Map jobMap = job.getJobDataMap();
				jobMap.put(group + SEPARATOR + name, id);

				// 添加参数
				if (null != params && params.size() > 0) {
					jobMap.putAll(params);
				}

				//
				CronTrigger trigger = newTrigger()
						.withIdentity(
								TRIGGER_MARK + SEPARATOR + name + SEPARATOR
										+ id,
								GROUP_MARK + SEPARATOR + group + SEPARATOR + id)
						.withSchedule(cronSchedule(this.cron)).build();
				this.scheduleTime = scheduler.scheduleJob(job, trigger);
				scheduler.start();
				this.state = JobState.STARTED;
				QuartzKit.addQuartzJob(this);
			}
		} catch (Exception e) {
			throw new RuntimeException("Can't start cron job.", e);
		}

	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

}
