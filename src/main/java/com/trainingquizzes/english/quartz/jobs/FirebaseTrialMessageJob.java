package com.trainingquizzes.english.quartz.jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.trainingquizzes.english.dto.FirebaseTrialMessageDto;

public class FirebaseTrialMessageJob extends QuartzJobBean {
	
	private static Logger logger = LoggerFactory.getLogger(FirebaseTrialMessageJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		String firebaseMessageTopic = jobDataMap.getString("firebaseMessageTopic");
		long questId = jobDataMap.getLong("questId");
		long userId = jobDataMap.getLong("userId");
		long trialId = jobDataMap.getLong("trialId");
		int trialNumber = jobDataMap.getInt("trialNumber");
		Message message = prepareFirebaseMessage(firebaseMessageTopic, questId, userId, trialId, trialNumber);
		sendFirebaseMessage(message, firebaseMessageTopic);
	}
	
	private static Message prepareFirebaseMessage(String firebaseMessageTopic, long questId, long userId, long trialId, int trialNumber) {
		
		FirebaseTrialMessageDto firebaseTrialMessageDto = new FirebaseTrialMessageDto(questId, userId, trialId, trialNumber);
		ObjectMapper objectMapper = new ObjectMapper();
		
		String firebaseTrialMessageString = null;
		try {
			firebaseTrialMessageString = objectMapper.writeValueAsString(firebaseTrialMessageDto);
		} catch (JsonProcessingException e) {
			logger.error("There was a problem trying to parse FirebaseTrialMessageDto to json: {} ", e.getMessage());
			e.printStackTrace();
		}
		
		return Message.builder()
				.putData("subscribed-quest", firebaseTrialMessageString)
				.setTopic(firebaseMessageTopic)
				.build();
	}
			
	private static void sendFirebaseMessage(Message message, String firebaseMessageTopic) {
		try {
			String response = FirebaseMessaging.getInstance().send(message);
			logger.info("Message successfully sent with topic: " + firebaseMessageTopic + ": " + response);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			logger.error("There was a problem trying to send Firebase message: ", e.getMessage());
		}
	}

}
