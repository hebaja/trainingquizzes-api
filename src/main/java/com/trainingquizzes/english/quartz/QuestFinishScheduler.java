package com.trainingquizzes.english.quartz;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trainingquizzes.english.quartz.jobs.FinishQuestJob;

public class QuestFinishScheduler {
	
	private static Logger logger = LoggerFactory.getLogger(QuestFinishScheduler.class);
	
	public static void schedule(long questId, ZonedDateTime startTime, Scheduler scheduler) {
		try {
			ZonedDateTime convertedZonedDateTimeStart = startTime.withZoneSameInstant(ZoneId.systemDefault());
			JobDetail jobDetail = buildJobDetail(questId);
			Trigger trigger = buildTrigger(jobDetail, convertedZonedDateTimeStart);
			logger.info("Scheduling finish quest id " + questId + " for " + startTime);
			
			scheduler.scheduleJob(jobDetail, trigger);
		} catch(SchedulerException e) {
			logger.error(e.getMessage());
		}
	}

	private static JobDetail buildJobDetail(long questId) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("questId", questId);
		
		return JobBuilder.newJob(FinishQuestJob.class)
				.withIdentity(UUID.randomUUID().toString(), "finish-quest-jobs")
				.withDescription("Finish Quest Job")
				.usingJobData(jobDataMap)
				.storeDurably()
				.build();
	}
	
	private static Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startTime) {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), "finish-quest-triggers")
				.startAt(Date.from(startTime.toInstant()))
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
				.withMisfireHandlingInstructionFireNow())
				.build();
	}
	

}
