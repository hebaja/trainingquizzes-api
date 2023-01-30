package com.trainingquizzes.english.quartz;

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

import com.trainingquizzes.english.quartz.jobs.FirebaseTrialMessageJob;

public class FirebaseTrialMessageScheduler {
	
	private static Logger logger = LoggerFactory.getLogger(FirebaseTrialMessageScheduler.class);
	
	public static void schedule(Scheduler scheduler, String firebaseMessageTopic, long questId, long userId, long trialId, ZonedDateTime startTime, int trialNumber) {
		try {
			JobDetail jobDetail = buildJobDetail(firebaseMessageTopic, questId, userId, trialId, trialNumber);
			Trigger trigger = buildTrigger(jobDetail, startTime);
			logger.info(String.format("Scheduling firebase trial message - topic: %s", firebaseMessageTopic));
			
			scheduler.scheduleJob(jobDetail, trigger);
		} catch(SchedulerException e) {
			logger.error(e.getMessage());
		}
	}
	
	private static JobDetail buildJobDetail(String firebaseMessageTopic, long questId, long userId, long trialId, int trialNumber) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("firebaseMessageKey", firebaseMessageTopic);
		jobDataMap.put("firebaseMessageTopic", firebaseMessageTopic);
		jobDataMap.put("questId", questId);
		jobDataMap.put("userId", userId);
		jobDataMap.put("trialId", trialId);
		jobDataMap.put("trialNumber", trialNumber);
		
		return JobBuilder.newJob(FirebaseTrialMessageJob.class)
				.withIdentity(UUID.randomUUID().toString(), "firebase-trial-message-jobs")
				.withDescription("Firebase Trial Message Job")
				.usingJobData(jobDataMap)
				.storeDurably()
				.build();
	}
	
	private static Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startTime) {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), "firebase-trial-message-triggers")
				.startAt(Date.from(startTime.toInstant()))
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
				.withMisfireHandlingInstructionFireNow())
				.build();
	}

}
