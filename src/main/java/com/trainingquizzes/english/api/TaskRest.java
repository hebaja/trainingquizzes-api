package com.trainingquizzes.english.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.dto.TaskDto;
import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.repository.TaskRepository;

@RestController
@CrossOrigin
@RequestMapping("api/task")
public class TaskRest {

	@Autowired
	private TaskRepository taskRepository;
	
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

}
