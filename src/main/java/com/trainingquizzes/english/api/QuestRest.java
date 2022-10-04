package com.trainingquizzes.english.api;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.config.FirebaseConfig;
import com.trainingquizzes.english.dto.QuestDto;
import com.trainingquizzes.english.form.QuestForm;
import com.trainingquizzes.english.form.QuestSubscribeForm;
import com.trainingquizzes.english.model.Quest;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Trial;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.QuestRepository;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.TemporaryTrialDataStoreRepository;
import com.trainingquizzes.english.repository.TrialRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.util.ScheduledThreadPool;

@RestController
@CrossOrigin
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
		
	@GetMapping("created-quests")
	public ResponseEntity<Page<QuestDto>> createdQuests(@RequestParam Long userId, Pageable pagination) {
		Optional<User> userOptional = userRepository.findById(userId);
		if(userOptional.isPresent()) {
			Optional<Page<Quest>> questsOptional = questRepository.findByUser(userOptional.get(), pagination);
			if(questsOptional.isPresent()) {
				Page<Quest> quests = questsOptional.get();
				Map<Long, List<User>> subscribedUsersMap = createSubscribedUsersMap(quests);
				
				return ResponseEntity.ok(QuestDto.convertToPageable(questsOptional.get(), subscribedUsersMap));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("subscribed-quests")
	public ResponseEntity<Page<QuestDto>> subscribedQuests(@RequestParam Long userId, Pageable pagination) {
		Optional<User> userOptional = userRepository.findById(userId);
		if(userOptional.isPresent()) {
			Optional<Page<Quest>> questsOptional = questRepository.findBySubscribedUserId(userId, pagination);
			if(questsOptional.isPresent()) {
				
				Map<Long,List<User>> subscribedUsersMap = createSubscribedUsersMap(questsOptional.get());
				
				return ResponseEntity.ok(QuestDto.convertToPageable(questsOptional.get(), subscribedUsersMap));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	private Map<Long, List<User>> createSubscribedUsersMap(Page<Quest> quests) {
		Map<Long, List<Long>> idsMap = quests.stream().collect(Collectors.toMap(Quest::getId, Quest::getSubscribedUsersIds));
		Map<Long, List<User>> questAndUsersMap = new HashMap<>();
		idsMap.forEach((a, b) -> {
			questAndUsersMap.put(a, userRepository.findAllById(b));
		});

		return questAndUsersMap;
	}
	
	@PutMapping
	public ResponseEntity<QuestDto> register(@RequestBody QuestForm form) {
		if(form != null) {
			User user = userRepository.findById(form.getUserId()).orElse(null);
			Subject subject = subjectRepository.findById(form.getSubjectId()).orElse(null);
			
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
			Quest quest = new Quest(
					form.getTitle(), 
					user, 
					subject, 
					form.getStartDate(), 
					form.getFinishDate(), 
					form.getTimeInterval(),
					ChronoUnit.DAYS);
			
			Quest savedQuest = questRepository.save(quest);
			scheduleQuestFinish(savedQuest);
			List<User> subscribedUsers = userRepository.findAllById(quest.getSubscribedUsersIds());
			
			return ResponseEntity.ok(new QuestDto(savedQuest, subscribedUsers));
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@DeleteMapping
	public ResponseEntity<List<QuestDto>> delete(@RequestParam Long questId) {
		if(questId != null) {
			Optional<Quest> questOptional = questRepository.findById(questId);
			Optional<User> userOptional = userRepository.findById(questOptional.get().getUser().getId());
			Optional<List<Quest>> questsOptional = questRepository.findAllByUser(userOptional.get());
			try {
				temporaryTrialDataStoreRepository.deleteAllByQyestId(questId);
				questRepository.delete(questOptional.get());
				
				return ResponseEntity.ok(QuestDto.convertToList(questsOptional.get()));
			} catch (EntityNotFoundException e) {
				
				throw new EntityNotFoundException();
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

				return ResponseEntity.ok(new QuestDto(finishQuest(questOptional.get()), subscribedUsers));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}

	@PostMapping("subscribe")
	public ResponseEntity<QuestDto> subscribe(@RequestBody QuestSubscribeForm form) {
		if(form != null) {
			Quest quest = questRepository.findById(form.getQuestId()).orElse(null);
			User user = userRepository.findById(form.getUserId()).orElse(null);
			if(quest != null && user != null) {
				user.addSubscribedQuestsId(quest.getId());
				quest.subscribeUser(user);
				userRepository.save(user);
				Quest savedQuest = questRepository.save(quest);
				List<User> subscribedUsers = userRepository.findAllById(quest.getSubscribedUsersIds());

				return ResponseEntity.ok(new QuestDto(savedQuest, subscribedUsers));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	private void finishQuestCaseNecessary(Quest quest) {
		LocalDateTime currentTime = LocalDateTime.now();
		if(currentTime.isAfter(quest.getFinishDate()) && !quest.isFinished()) {
			finishQuest(quest);
		}
	}

	private Quest finishQuest(Quest quest) {
		quest.setFinished(true);
		temporaryTrialDataStoreRepository.deleteAllByQyestId(quest.getId());
		quest.finishResult();
		return questRepository.save(quest);
	}
	
	private void scheduleQuestFinish(Quest quest) {
		long questDurationInMinutes = ChronoUnit.MINUTES.between(quest.getStartDate(), quest.getFinishDate());
		ScheduledExecutorService threadPool = ScheduledThreadPool.getScheduledThreadPool();
		threadPool.schedule(new Runnable() {
			@Override
			public void run() {
				finishQuest(quest);
			}
		}, questDurationInMinutes, TimeUnit.MINUTES);
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
