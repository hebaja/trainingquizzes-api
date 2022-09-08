package com.trainingquizzes.english.api;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.dto.TaskDto;
import com.trainingquizzes.english.dto.TrialTasksDto;
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
@RequestMapping("api/task")
public class TaskRest {

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TrialRepository trialRepository;
	
	@Autowired
	private TemporaryTrialDataStoreRepository temporaryTrialDataStoreRepository;
	
	@GetMapping
	public ResponseEntity<List<TaskDto>> tasksBySubjectId(@RequestParam("subjectId") Long subjectId) {
		if(subjectId != null) {
			Optional<List<Task>> tasksOptional = taskRepository.findAllBySubjectId(subjectId);
			if(tasksOptional.isPresent()) {
				return ResponseEntity.ok(TaskDto.convertList(tasksOptional.get()));
			}
		}
		
		return ResponseEntity.badRequest().build(); 
	}
	
	@PostMapping("trial-tasks")
	public ResponseEntity<TrialTasksDto> triailTasks(@RequestBody TemporaryTrialDataStoreForm form) {
		if(form != null) {
			Optional<User> optionalUser = userRepository.findById(form.getUserId());
			Optional<Trial> optionalTrial = trialRepository.findById(form.getTrialId());
			if(optionalTrial.isPresent() && optionalUser.isPresent()) {
				Optional<TemporaryTrialDataStore> optionalTemporatyTrialData = 
						temporaryTrialDataStoreRepository.findByTrialUserAndTrialNumber(optionalTrial.get(), optionalUser.get(), form.getTrialNumber());
				if(optionalTemporatyTrialData.isPresent()) {
					
					return ResponseEntity.ok(new TrialTasksDto(optionalTemporatyTrialData.get())); 
				} else {
					Optional<List<Task>> optionalTasks = taskRepository.findAllBySubjectId(optionalTrial.get().getQuest().getSubject().getId());
					if(!optionalTasks.isEmpty()) {
						Collections.shuffle(optionalTasks.get());
						List<Task> reducedTasksList = optionalTasks.get().stream().limit(10).collect(Collectors.toList());
						TemporaryTrialDataStore temporaryTrialData = 
								new TemporaryTrialDataStore(optionalTrial.get(), optionalUser.get(), form.getTrialNumber(), reducedTasksList);
						temporaryTrialDataStoreRepository.save(temporaryTrialData);
						
						return ResponseEntity.ok(new TrialTasksDto(temporaryTrialData));
					}
				}
			}
		}
		
		return ResponseEntity.badRequest().build();
	}


}
