package com.trainingquizzes.english.quartz.jobs;

import java.util.Optional;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.trainingquizzes.english.dto.FirebaseTrialMessageDto;
import com.trainingquizzes.english.model.Quest;
import com.trainingquizzes.english.repository.QuestRepository;
import com.trainingquizzes.english.repository.TemporaryTrialDataStoreRepository;

@Component
public class FinishQuestJob extends QuartzJobBean{
	
	private static Logger logger = LoggerFactory.getLogger(FinishQuestJob.class);
	
	@Autowired
	private QuestRepository questRepository;
	
	@Autowired
	private TemporaryTrialDataStoreRepository temporaryTrialDataStoreRepository;
	
	@Value("${spring-english-training-quizzes-default-domain}")
	private String defaultDomain;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		long questId = jobDataMap.getLong("questId");
		Optional<Quest> questOptional = questRepository.findById(questId);
		
		if(questOptional.isPresent()) {
			Quest quest = questOptional.get();
			quest.setFinished(true);
			quest.setPin(null);
			temporaryTrialDataStoreRepository.deleteAllByQyestId(quest.getId());
			logger.info(String.format("finishing quest id: %d", questId));
			
			String firebaseMessageTopic = String.format("%d-%s", quest.getId(), quest.getUser().getUsername());
			Message message = prepareFirebaseMessage(firebaseMessageTopic);
			sendFirebaseMessage(message, firebaseMessageTopic);
			
			questRepository.save(quest);
		}
	}
	
	private static Message prepareFirebaseMessage(String firebaseMessageTopic) {
		return Message.builder()
				.putData("unsubscribe-quest", firebaseMessageTopic)
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
