 package com.trainingquizzes.english.api;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.form.QuestForm;
import com.trainingquizzes.english.model.Quest;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.quartz.EmailScheduler;
import com.trainingquizzes.english.quartz.QuestFinishScheduler;
import com.trainingquizzes.english.quartz.ScheduledEmail;
import com.trainingquizzes.english.repository.QuestRepository;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleEmailRest {
	
	@Autowired
	private Scheduler scheduler;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private QuestRepository questRepository;

	@Value("${spring.mail.username}")
	private String senderEmail;
	
	Logger logger = LoggerFactory.getLogger(ScheduleEmailRest.class);
	
	@GetMapping("75d3b781-12b4-43f8-85b6-8c6f8b752fde")
	public ResponseEntity<String> schedule() {
		ZonedDateTime zonedDateTimeNow = ZonedDateTime.now().plusMinutes(2L);
		
		ScheduledEmail scheduledEmail = new ScheduledEmail(senderEmail, "hebaja@yahoo.com.br", "Testing schedule email", "There should be a link here", zonedDateTimeNow);
		try {
			EmailScheduler.schedule(scheduler, scheduledEmail);
			return ResponseEntity.ok("ok");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("5049f7eb-cee2-42fb-a4ca-06670c46b70b")
	public ResponseEntity<String> createQuest(@RequestBody QuestForm form) {
		
		logger.info(form.getTimeZone());
		logger.info(form.getStartDate().toString());
		logger.info(form.getFinishDate().toString());
		
		User user = userRepository.findById(form.getUserId()).get();
		Subject subject = subjectRepository.findById(form.getSubjectId()).get();
		
		ZonedDateTime zonedStartDate = ZonedDateTime.of(form.getStartDate(), ZoneId.of(form.getTimeZone()));
		ZonedDateTime zonedFinishDate = ZonedDateTime.of(form.getFinishDate(), ZoneId.of(form.getTimeZone()));
						
		Quest quest = new Quest(form.getTitle(), user, subject, zonedStartDate, zonedFinishDate, form.getTimeZone(), form.getTimeInterval(), ChronoUnit.MINUTES);
		Quest savedQuest = questRepository.save(quest);
		QuestFinishScheduler.schedule(savedQuest.getId(), savedQuest.getFinishDate(), scheduler);
		
		return ResponseEntity.ok("ok");
	}
}
