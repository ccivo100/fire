package com.poicom.basic.plugin.quartz;

import java.util.List;

import org.quartz.SchedulerFactory;

import com.google.common.collect.Lists;
import com.poicom.basic.plugin.quartz.job.QuartzJob;

/**
 * 
 * @author wangrenhui on 14-4-21.
 *
 */
public class QuartzKit {
	
	private static SchedulerFactory schedulerFactory;

	private static List<QuartzJob> quartzJobs = Lists.newArrayList();

	private QuartzKit() {

	}

	/**
	 * 获取指定任务
	 * 
	 * @param quartzKey
	 * @return
	 */
	public static QuartzJob getJob(QuartzKey quartzKey) {
		for (QuartzJob quartzJob : quartzJobs) {
			if (quartzJob.getQuartzKey().equals(quartzKey)) {
				return quartzJob;
			}
		}
		return null;
	}

	/**
	 * 停止指定任务
	 * 
	 * @param quartzKey
	 */
	public static void stopJob(QuartzKey quartzKey) {
		for (QuartzJob quartzJob : quartzJobs) {
			if (quartzJob.getQuartzKey().equals(quartzKey)) {
				quartzJob.stop();
			}
		}
	}

	/**
	 * 暂停指定任务
	 * 
	 * @param quartzKey
	 */
	public static void pauseJob(QuartzKey quartzKey) {
		for (QuartzJob quartzJob : quartzJobs) {
			if (quartzJob.getQuartzKey().equals(quartzKey)) {
				quartzJob.pause();
			}
		}
	}

	/**
	 * 重启指定任务
	 * 
	 * @param quartzKey
	 */
	public static void resumeJob(QuartzKey quartzKey) {
		for (QuartzJob quartzJob : quartzJobs) {
			if (quartzJob.getQuartzKey().equals(quartzKey)) {
				quartzJob.resume();
			}
		}
	}

	public static SchedulerFactory getSchedulerFactory() {
		return schedulerFactory;
	}

	public static void setSchedulerFactory(SchedulerFactory schedulerFactory) {
		QuartzKit.schedulerFactory = schedulerFactory;
	}

	public static List<QuartzJob> getQuartzJobs() {
		return quartzJobs;
	}

	public static void setQuartzJobs(List<QuartzJob> quartzJobs) {
		QuartzKit.quartzJobs = quartzJobs;
	}

	public static void addQuartzJob(QuartzJob startedJob) {
		QuartzKit.quartzJobs.add(startedJob);
	}

	public static void removeQuartzJob(QuartzJob startedJob) {
		QuartzKit.quartzJobs.remove(startedJob);
	}

}
