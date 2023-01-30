package com.trainingquizzes.english.quartz.jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class EmailJob extends QuartzJobBean {
	
	@Autowired
	private JavaMailSender mailSender;

	private static Logger logger = LoggerFactory.getLogger(EmailJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		
		String senderEmail = jobDataMap.getString("senderEmail");
		String recipientEmail = jobDataMap.getString("email");
		String subject = jobDataMap.getString("subject");
		String body = jobDataMap.getString("body");

		sendMail(senderEmail, recipientEmail, subject, body);
	}
	
	private void sendMail(String senderEmail, String recipientEmail, String subject, String body) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(senderEmail);
		mailMessage.setTo(recipientEmail);
		mailMessage.setSubject(subject);
		mailMessage.setText(body);
		logger.info(String.format("Sending email to %s", recipientEmail));
	
		mailSender.send(mailMessage);
	}

}
