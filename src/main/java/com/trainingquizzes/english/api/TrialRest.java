package com.trainingquizzes.english.api;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.config.FirebaseConfig;
import com.trainingquizzes.english.dto.TrialDto;
import com.trainingquizzes.english.dto.TrialTaskDto;
import com.trainingquizzes.english.form.SaveTemporaryTrialDataStore;
import com.trainingquizzes.english.form.TemporaryTrialDataStoreForm;
import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.model.TemporaryTrialDataStore;
import com.trainingquizzes.english.model.Trial;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.TaskRepository;
import com.trainingquizzes.english.repository.TemporaryTrialDataStoreRepository;
import com.trainingquizzes.english.repository.TrialRepository;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("api/trial")
public class TrialRest {
	
	@Autowired
	private TemporaryTrialDataStoreRepository temporaryTrialDataStoreRepository;
	
	@Autowired
	private TrialRepository trialRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	Logger logger = LoggerFactory.getLogger(TrialRest.class);
	
	@PostMapping("open")
	public ResponseEntity<TrialTaskDto> triailTasks(@RequestBody TemporaryTrialDataStoreForm form) {
		if(form != null) {
			Optional<User> optionalUser = userRepository.findById(form.getUserId());
			Optional<Trial> optionalTrial = trialRepository.findById(form.getTrialId());
			if(optionalTrial.isPresent() && optionalUser.isPresent()) {
				
				Trial trial = optionalTrial.get();
				User user = optionalUser.get();
				
				Optional<TemporaryTrialDataStore> optionalTemporatyTrialData = 
						temporaryTrialDataStoreRepository.findByTrialUserAndTrialNumber(trial, user, form.getTrialNumber());
				if(optionalTemporatyTrialData.isPresent()) {
					TemporaryTrialDataStore temporaryTrialDataStore = optionalTemporatyTrialData.get();
					Task task = temporaryTrialDataStore.getTasksIndex() < 10 ? temporaryTrialDataStore.getReducedTasksList().get(temporaryTrialDataStore.getTasksIndex()) : null;
					if(task != null && task.isShuffleOptions()) Collections.shuffle(task.getOptions());
					
					return ResponseEntity.ok(new TrialTaskDto(temporaryTrialDataStore, task)); 
				} else {
					Optional<List<Task>> optionalTasks = taskRepository.findAllBySubjectId(trial.getQuest().getSubject().getId());
					if(!optionalTasks.isEmpty()) {
						Collections.shuffle(optionalTasks.get());
						List<Task> reducedTasksList = optionalTasks.get().stream().limit(10).collect(Collectors.toList());
						TemporaryTrialDataStore temporaryTrialData = 
								new TemporaryTrialDataStore(trial, user, form.getTrialNumber(), reducedTasksList);
						
						trial.setScore(0.0);
						trialRepository.save(trial);
						temporaryTrialDataStoreRepository.save(temporaryTrialData);
						
						Task task = temporaryTrialData.getTasksIndex() < 10 ? temporaryTrialData.getReducedTasksList().get(temporaryTrialData.getTasksIndex()) : null;
						if(task != null && task.isShuffleOptions()) Collections.shuffle(task.getOptions());
						
						return ResponseEntity.ok(new TrialTaskDto(temporaryTrialData, task));
					}
				}
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("update")
	public ResponseEntity<TrialTaskDto> saveTrial(@RequestBody SaveTemporaryTrialDataStore form) {
		Optional<TemporaryTrialDataStore> optionalTrialItem = temporaryTrialDataStoreRepository.findById(form.getId());
		if(optionalTrialItem.isPresent() && !optionalTrialItem.get().isFinished()) {
			TemporaryTrialDataStore temporaryTrialDataStore = optionalTrialItem.get();
			double score = temporaryTrialDataStore.getScore();
			if(!Double.isNaN(score)) {
				Trial trial = temporaryTrialDataStore.getTrial();
				if(form.isCorrect()) score++;
				temporaryTrialDataStore.iterateTasksIndex();
				if(temporaryTrialDataStore.getTasksIndex() >= 10) {
					temporaryTrialDataStore.setFinished(true);
					trial.setFinished(true);
				}
				temporaryTrialDataStore.setScore(score);
				trial.setScore(score);
				trialRepository.save(trial);
				
				Task task = temporaryTrialDataStore.getTasksIndex() < 10 ? temporaryTrialDataStore.getReducedTasksList().get(temporaryTrialDataStore.getTasksIndex()) : null;
				if(task != null && task.isShuffleOptions()) Collections.shuffle(task.getOptions());
				
				return ResponseEntity.ok(new TrialTaskDto(temporaryTrialDataStoreRepository.save(temporaryTrialDataStore), task));
			}
		} else {
			
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
		
		return ResponseEntity.badRequest().build();
	}

}
