package com.trainingquizzes.english.api;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@CrossOrigin
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
	
	@PostMapping("open")
	public ResponseEntity<TrialTaskDto> triailTasks(@RequestBody TemporaryTrialDataStoreForm form) {
		if(form != null) {
			Optional<User> optionalUser = userRepository.findById(form.getUserId());
			Optional<Trial> optionalTrial = trialRepository.findById(form.getTrialId());
			if(optionalTrial.isPresent() && optionalUser.isPresent()) {
				Optional<TemporaryTrialDataStore> optionalTemporatyTrialData = 
						temporaryTrialDataStoreRepository.findByTrialUserAndTrialNumber(optionalTrial.get(), optionalUser.get(), form.getTrialNumber());
				if(optionalTemporatyTrialData.isPresent()) {
					
					return ResponseEntity.ok(new TrialTaskDto(optionalTemporatyTrialData.get())); 
				} else {
					Optional<List<Task>> optionalTasks = taskRepository.findAllBySubjectId(optionalTrial.get().getQuest().getSubject().getId());
					if(!optionalTasks.isEmpty()) {
						Collections.shuffle(optionalTasks.get());
						List<Task> reducedTasksList = optionalTasks.get().stream().limit(10).collect(Collectors.toList());
						TemporaryTrialDataStore temporaryTrialData = 
								new TemporaryTrialDataStore(optionalTrial.get(), optionalUser.get(), form.getTrialNumber(), reducedTasksList);
						temporaryTrialDataStoreRepository.save(temporaryTrialData);
						
						return ResponseEntity.ok(new TrialTaskDto(temporaryTrialData));
					}
				}
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("update")
	public ResponseEntity<TrialTaskDto> saveTrial(@RequestBody SaveTemporaryTrialDataStore form) {
		
		if(form != null) {
			Optional<TemporaryTrialDataStore> optionalTrialItem = temporaryTrialDataStoreRepository.findById(form.getId());
			
			if(optionalTrialItem.isPresent()) {
				TemporaryTrialDataStore temporaryTrialDataStore = optionalTrialItem.get();
								
				if(!temporaryTrialDataStore.isFinished()) {
					
					temporaryTrialDataStore.iterateTasksIndex();

					if(form.isCorrect()) {
						temporaryTrialDataStore.setScore(temporaryTrialDataStore.getScore() + 1);
					}

					if(temporaryTrialDataStore.getTasksIndex() >= 10) {
						temporaryTrialDataStore.setFinished(true);
						temporaryTrialDataStore.getTrial().setFinished(true);
					}

					TemporaryTrialDataStore savedTemporaryTrialDataStore = temporaryTrialDataStoreRepository.save(temporaryTrialDataStore);
					savedTemporaryTrialDataStore.getTrial().setScore(savedTemporaryTrialDataStore.getScore());
					trialRepository.save(savedTemporaryTrialDataStore.getTrial());

					return ResponseEntity.ok(new TrialTaskDto(temporaryTrialDataStore));
				} else {
					return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
				}
			}
		}
		return ResponseEntity.badRequest().build();
	}

}
