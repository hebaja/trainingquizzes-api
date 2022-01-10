package com.trainingquizzes.english.api;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.trainingquizzes.english.dto.TaskDto;
import com.trainingquizzes.english.dto.SubjectDto;
import com.trainingquizzes.english.dto.SubjectWithTasksDto;
import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.form.ExerciseForm;
import com.trainingquizzes.english.model.Exercise;
import com.trainingquizzes.english.model.ExercisesQuantity;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.ExerciseRepository;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.TaskRepository;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("/api/subject")
public class SubjectRest {
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@GetMapping("all")
	@Cacheable(value = "subjectsList")
	public ResponseEntity<List<SubjectDto>> subjects() {
		List<Subject> subjects = subjectRepository.findAll();
		if(subjects != null) {
			return ResponseEntity.ok(SubjectDto.convertList(subjects));	
		}
		return ResponseEntity.notFound().build();
		
	}
	
	@GetMapping("{subjectId}")
	@Cacheable(value = "subject")
	public ResponseEntity<SubjectWithTasksDto> subjectById(@PathVariable("subjectId") Long subjectId) {
		Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
		Subject subject = subjectOptional.orElse(null);
		if(subject != null) {
			SubjectWithTasksDto subjectWithTaskDto = SubjectWithTasksDto.convertFromSubject(subject);
			return ResponseEntity.ok(subjectWithTaskDto);	
		}
		return ResponseEntity.notFound().build();
	}
}