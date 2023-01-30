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

import com.trainingquizzes.english.quartz.jobs.EmailJob;

public class EmailScheduler {
	
	private static Logger logger = LoggerFactory.getLogger(EmailScheduler.class);
	
	public static void schedule(Scheduler scheduler, ScheduledEmail scheduledEmail) {
		try {
			JobDetail jobDetail = buildJobDetail(scheduledEmail);
			Trigger trigger = buildTrigger(jobDetail, scheduledEmail.getStartTime());
			logger.info("Scheduling email for " + scheduledEmail.getRecipientEmail() + " for " + scheduledEmail.getStartTime());

			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static JobDetail buildJobDetail(ScheduledEmail scheduleEmailRequest) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("senderEmail", scheduleEmailRequest.getSenderEmail());
        jobDataMap.put("email", scheduleEmailRequest.getRecipientEmail());
        jobDataMap.put("subject", scheduleEmailRequest.getSubject());
        jobDataMap.put("body", scheduleEmailRequest.getBody());

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }
	
	private static Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startTime) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .startAt(Date.from(startTime.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withMisfireHandlingInstructionFireNow())
                .build();
    }

}
