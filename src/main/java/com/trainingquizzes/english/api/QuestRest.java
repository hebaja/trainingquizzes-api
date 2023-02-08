package com.trainingquizzes.english.api;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.trainingquizzes.english.dto.QuestDto;
import com.trainingquizzes.english.form.QuestForm;
import com.trainingquizzes.english.form.QuestSubscribeForm;
import com.trainingquizzes.english.model.Quest;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Trial;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.quartz.EmailScheduler;
import com.trainingquizzes.english.quartz.FirebaseTrialMessageScheduler;
import com.trainingquizzes.english.quartz.QuestFinishScheduler;
import com.trainingquizzes.english.quartz.ScheduledEmail;
import com.trainingquizzes.english.repository.QuestRepository;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.TemporaryTrialDataStoreRepository;
import com.trainingquizzes.english.repository.TrialRepository;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("/api/quest")
public class QuestRest {
	
	Logger logger = LoggerFactory.getLogger(QuestRest.class);
	
	@Autowired
	private QuestRepository questRepository;
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TrialRepository trialRepository;

	@Autowired 
	private TemporaryTrialDataStoreRepository temporaryTrialDataStoreRepository;
	
	@Autowired
	private Scheduler scheduler;
	
	@Value("${spring-english-training-quizzes-default-domain}")
	private String defaultDomain;
	
	@Value("${spring.mail.username}")
	private String senderEmail;
	
	@GetMapping
	public ResponseEntity<QuestDto> questById(@RequestParam Long questId, @RequestParam(required = false) Long userId) throws CloneNotSupportedException {
		if(userId == null) {
			Quest quest = questRepository.findById(questId).orElse(null);
			if(quest != null) {
				finishQuestCaseNecessary(quest);
				List<User> subscribedUsers = userRepository.findAllById(quest.getSubscribedUsersIds());
				
				return ResponseEntity.ok(new QuestDto(quest, subscribedUsers));
			}
		} else {
			User user = userRepository.findById(userId).orElse(null);
			if(user != null) {
				Quest quest = questRepository.findById(questId).orElse(null);
				if(quest != null) {
					finishQuestCaseNecessary(quest);
					List<Trial> trials = quest.getTrials().stream().filter(trial -> trial.getSubscribedUser().equals(user)).collect(Collectors.toList());
					Quest clonedQuest = (Quest) quest.clone();
					clonedQuest.getTrials().clear();
					clonedQuest.setTrials(trials);
					List<User> subscribedUsers = userRepository.findAllById(quest.getSubscribedUsersIds());
					
					return ResponseEntity.ok(new QuestDto(clonedQuest, subscribedUsers));
				}
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("pin")
	public ResponseEntity<QuestDto> questByPin(@RequestParam String pin) {
		if(pin != null) {
			Optional<Quest> questOptional = questRepository.findByPin(pin);
			if(questOptional.isPresent()) {
				List<User> subscribedUsers = userRepository.findAllById(questOptional.get().getSubscribedUsersIds());
				
				return ResponseEntity.ok(new QuestDto(questOptional.get(), subscribedUsers));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
		
	@GetMapping("created-quests")
	public ResponseEntity<Page<QuestDto>> createdQuests(@RequestParam(name = "query", required = false) String query , @RequestParam Long userId, Pageable pagination) {
		if(query != null) {
			Optional<User> userOptional = userRepository.findById(userId);
			if(userOptional.isPresent()) {
				String searchQuery = "%" + query + "%";
				Optional<Page<Quest>> questsOptional = questRepository.findByTitleLikeIgnoreCaseAndUser(searchQuery, userOptional.get(), pagination);
				if(questsOptional.isPresent()) {
					Page<Quest> quests = questsOptional.get();
					Map<Long, List<User>> subscribedUsersMap = createSubscribedUsersMap(quests);
					
					return ResponseEntity.ok(QuestDto.convertToPageable(questsOptional.get(), subscribedUsersMap));
				}
			}
		} else {
			Optional<User> userOptional = userRepository.findById(userId);
			if(userOptional.isPresent()) {
				Optional<Page<Quest>> questsOptional = questRepository.findByUser(userOptional.get(), pagination);
				if(questsOptional.isPresent()) {
					Page<Quest> quests = questsOptional.get();
					Map<Long, List<User>> subscribedUsersMap = createSubscribedUsersMap(quests);
					
					return ResponseEntity.ok(QuestDto.convertToPageable(questsOptional.get(), subscribedUsersMap));
				}
			}			
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("subscribed-quests")
	public ResponseEntity<Page<QuestDto>> subscribedQuests(@RequestParam(name = "query", required = false) String query, @RequestParam Long userId, Pageable pagination) {
		if(query != null) {
			System.out.println(query);
			Optional<User> userOptional = userRepository.findById(userId);
			if(userOptional.isPresent()) {
				Optional<Page<Quest>> questsOptional = questRepository.findByTitleLikeIgnoreCaseAndSubscribedUserId(query, userId, pagination);
				if(questsOptional.isPresent()) {
					System.out.println(query);
					
					questsOptional.get().forEach(quest -> System.out.println(quest.getTitle()));
					
					Map<Long,List<User>> subscribedUsersMap = createSubscribedUsersMap(questsOptional.get());
					
					return ResponseEntity.ok(QuestDto.convertToPageable(questsOptional.get(), subscribedUsersMap));
				}
			}
		} else {
			Optional<User> userOptional = userRepository.findById(userId);
			if(userOptional.isPresent()) {
				Optional<Page<Quest>> questsOptional = questRepository.findBySubscribedUserId(userId, pagination);
				if(questsOptional.isPresent()) {
					Map<Long,List<User>> subscribedUsersMap = createSubscribedUsersMap(questsOptional.get());
					
					return ResponseEntity.ok(QuestDto.convertToPageable(questsOptional.get(), subscribedUsersMap));
				}
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	private Map<Long, List<User>> createSubscribedUsersMap(Page<Quest> quests) {
		Map<Long, List<Long>> idsMap = quests.stream().collect(Collectors.toMap(Quest::getId, Quest::getSubscribedUsersIds));
		Map<Long, List<User>> questAndUsersMap = new HashMap<>();
		idsMap.forEach((a, b) -> questAndUsersMap.put(a, userRepository.findAllById(b)));

		return questAndUsersMap;
	}
	
	@PutMapping
	public ResponseEntity<QuestDto> register(@RequestBody QuestForm form) {
		if(form != null) {
			Optional<User> userOptional = userRepository.findById(form.getUserId());
			Optional<Subject> subjectOptional = subjectRepository.findById(form.getSubjectId());
			if(userOptional.isPresent() && subjectOptional.isPresent()) {
				if(form.getId() != null) {
					Optional<Quest> optionalQuest = questRepository.findById(form.getId());
					if(optionalQuest.isPresent()) {
						Quest quest = optionalQuest.get();
						quest.setTitle(form.getTitle());
						Quest savedQuest = questRepository.save(quest);
						List<User> subscribedUsers = userRepository.findAllById(quest.getSubscribedUsersIds());
						
						return ResponseEntity.ok(new QuestDto(savedQuest, subscribedUsers));
					}
				}
				
				ZonedDateTime startDate = ZonedDateTime.of(form.getStartDate(), ZoneId.of(form.getTimeZone()));
				ZonedDateTime finishDate = ZonedDateTime.of(form.getFinishDate(), ZoneId.of(form.getTimeZone()));
				
				Quest quest = new Quest(
						form.getTitle(), 
						userOptional.get(), 
						subjectOptional.get(), 
						startDate,
						finishDate,
						form.getTimeZone(),
						form.getTimeInterval(),
						ChronoUnit.DAYS);
				
				Quest savedQuest = questRepository.save(quest);
				QuestFinishScheduler.schedule(savedQuest.getId(), savedQuest.getFinishDate(), scheduler);
				List<User> subscribedUsers = userRepository.findAllById(quest.getSubscribedUsersIds());
				
				return ResponseEntity.ok(new QuestDto(savedQuest, subscribedUsers));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@DeleteMapping("{questId}")
	public ResponseEntity<List<QuestDto>> delete(@PathVariable("questId") Long questId) {
		if(questId != null) {
			Optional<Quest> questOptional = questRepository.findById(questId);
			Optional<User> userOptional = userRepository.findById(questOptional.get().getUser().getId());
			Optional<List<Quest>> questsOptional = questRepository.findAllByUser(userOptional.get());
			try {
				temporaryTrialDataStoreRepository.deleteAllByQyestId(questId);
				questRepository.delete(questOptional.get());
				
				return ResponseEntity.ok(QuestDto.convertToList(questsOptional.get()));
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				return ResponseEntity.notFound().build();
			}
		}
		
		return ResponseEntity.badRequest().build(); 
	}
	
	@GetMapping("finish")
	public ResponseEntity<QuestDto> finish(@RequestParam("questId") Long questId) {
		if(questId != null) {
			Optional<Quest> questOptional = questRepository.findById(questId);
			if(questOptional.isPresent()) {
				List<User> subscribedUsers = userRepository.findAllById(questOptional.get().getSubscribedUsersIds());
				Quest savedQuest = finishQuest(questOptional.get());

				return ResponseEntity.ok(new QuestDto(finishQuest(savedQuest), subscribedUsers));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}

	@PostMapping("subscribe")
	public ResponseEntity<QuestDto> subscribe(@RequestBody QuestSubscribeForm form) {
		if(form != null) {
			Optional<Quest> questOptional = questRepository.findById(form.getQuestId());
			Optional<User> userOptional = userRepository.findById(form.getUserId());
			if(questOptional.isPresent() && userOptional.isPresent()) {
				User user = userOptional.get();
				Quest quest = questOptional.get();
				user.addSubscribedQuestsId(quest.getId());
				quest.subscribeUser(user);
				userRepository.save(user);
				Quest savedQuest = questRepository.save(quest);
				List<User> subscribedUsers = userRepository.findAllById(quest.getSubscribedUsersIds());
				
				List<Trial> userTrialsInQuest = quest.getTrials().stream().filter(trial -> trial.getSubscribedUser().getId().equals(user.getId())).collect(Collectors.toList());
				
				userTrialsInQuest.forEach(trial -> {
					if(trial.getTrialNumber() > 1) {
						ScheduledEmail scheduledEmail = 
								new ScheduledEmail(
										senderEmail,
										user.getEmail(), 
										"Training Quizzes - Trial #" + trial.getTrialNumber() + " is available", 
										String.format("There is a new trial on %s quest available for you. Access the following link to take it: %s/#/trial-quiz?userId=%d&trialId=%d&trialNumber=%d", 
												quest.getTitle(), defaultDomain, user.getId(), trial.getId(), trial.getTrialNumber()),
										trial.getStartDate());
						
						EmailScheduler.schedule(scheduler, scheduledEmail);
						String firebaseMessageTopic = String.format("%d-%s", quest.getId(), quest.getUser().getUsername());  																		
						FirebaseTrialMessageScheduler.schedule(scheduler, firebaseMessageTopic, quest.getId(), user.getId(), trial.getId(), trial.getStartDate(), trial.getTrialNumber());
					}
				});
				
				return ResponseEntity.ok(new QuestDto(savedQuest, subscribedUsers));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	private void finishQuestCaseNecessary(Quest quest) {
		LocalDateTime currentTime = LocalDateTime.now();
		ZonedDateTime zonedCurrenttime = ZonedDateTime.of(currentTime, ZoneId.of(quest.getTimeZone()));
		ZonedDateTime convertedFinishZonedDateTime = ZonedDateTime.of(quest.getFinishDate().toLocalDateTime(), ZoneId.of(quest.getTimeZone()));
		if(zonedCurrenttime.isAfter(convertedFinishZonedDateTime) && !quest.isFinished()) {
			finishQuest(quest);
		}
	}

	private Quest finishQuest(Quest quest) {
		quest.setFinished(true);
		quest.setPin(null);
		temporaryTrialDataStoreRepository.deleteAllByQyestId(quest.getId());
		
		return questRepository.save(quest);
	}
	
	@GetMapping("unsubscribe-user")
	public ResponseEntity<QuestDto> unsubscribeUser(@RequestParam("userId") Long userId, @RequestParam("questId") Long questId) {
		if(userId != null && questId != null) {
			Optional<User> userOptional = userRepository.findById(userId);
			Optional<Quest> questOptional = questRepository.findById(questId);
			if(userOptional.isPresent() && questOptional.isPresent()) {
				questOptional.get().unsubscribeUser(userOptional.get());
				userOptional.get().removeSubscribedquestsId(questOptional.get().getId());
				temporaryTrialDataStoreRepository.deleteAllByUser(userOptional.get());
				trialRepository.deleteAllBySubscribedUserAndQuest(userOptional.get(), questOptional.get());
				userRepository.save(userOptional.get());
				Quest savedQuest = questRepository.save(questOptional.get());
				List<User> subscribedUsers = userRepository.findAllById(questOptional.get().getSubscribedUsersIds());
				
				return ResponseEntity.ok(new QuestDto(savedQuest, subscribedUsers));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}

}
