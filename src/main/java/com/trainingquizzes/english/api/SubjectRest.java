package com.trainingquizzes.english.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.trainingquizzes.english.form.SubjectForm;
import com.trainingquizzes.english.form.TaskForm;
import com.trainingquizzes.english.model.Exercise;
import com.trainingquizzes.english.model.ExercisesQuantity;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.model.TaskOption;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.ExerciseRepository;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.TaskRepository;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("/api/subjects")
public class SubjectRest {

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping
	@Cacheable(value = "subjectsList")
	public ResponseEntity<List<SubjectWithTasksDto>> subjects() {
		List<Subject> subjects = subjectRepository.findAll();
		if (subjects != null) {
			return ResponseEntity.ok(SubjectWithTasksDto.convertList(subjects));
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("{subjectId}")
	@Cacheable(value = "subject")
	public ResponseEntity<SubjectWithTasksDto> subjectById(@PathVariable("subjectId") Long subjectId) {
		Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
		Subject subject = subjectOptional.orElse(null);
		if (subject != null) {
			SubjectWithTasksDto subjectWithTaskDto = SubjectWithTasksDto.convertFromSubject(subject);
			return ResponseEntity.ok(subjectWithTaskDto);
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("{subjectId}")
	@CacheEvict(value = "subjectsList", allEntries = true)
	public ResponseEntity<List<SubjectWithTasksDto>> delete(@PathVariable("subjectId") Long subjectId) {
		subjectRepository.deleteById(subjectId);
		List<Subject> subjects = subjectRepository.findAll();
		return ResponseEntity.ok(SubjectWithTasksDto.convertList(subjects));
	}
	
	@PutMapping
	@CacheEvict(value = "subjectsList", allEntries = true)
	public ResponseEntity<List<SubjectWithTasksDto>> update(@RequestBody SubjectForm form) {
		
		if(form != null) {
			Subject subject = null;
			if(form.getId() != null) {
				subject = subjectRepository.findById(form.getId()).orElse(null);
			} else {
				subject = new Subject();
				subject.setTasks(new ArrayList<Task>());
				User user = userRepository.findById(form.getUser().getId()).orElse(null);
				if(user == null) {
					return ResponseEntity.badRequest().build();
				}
				subject.setUser(user);
			}
			
			if(subject != null) {
				subject.setTitle(form.getTitle());
				subject.setLevel(form.getLevel());
				if(form.getId() != null) {
					List<Task> foundTasksInForm = findTasksInForm(form, subject);
					List<Task> tasksToBeRemoved = findTasksToBeRemoved(subject, foundTasksInForm);
					removeTasks(subject, tasksToBeRemoved);
					updateExistingTasks(form, foundTasksInForm);
				}
				createNewTasks(form, subject);
				subjectRepository.save(subject);
				List<Subject> subjects = subjectRepository.findAll();
				return ResponseEntity.ok(SubjectWithTasksDto.convertList(subjects));
			}
		}
		return ResponseEntity.badRequest().build();
	}

	private void createNewTasks(SubjectForm form, Subject subject) {
		form.getTasks().forEach(taskForm -> {
			if(taskForm.getId() == null) {
				Task task = new Task();
				task.setPrompt(taskForm.getPrompt());
				task.setSubject(subject);
				
				List<TaskOption> options = new ArrayList<TaskOption>();
				
				taskForm.getOptions().forEach(option -> {
					options.add(new TaskOption(option.getPrompt(), option.isCorrect())); 
				});
				
				task.setOptions(options);
				subject.getTasks().add(task);
			}
		});
	}

	private void updateExistingTasks(SubjectForm form, List<Task> foundTasksInForm) {
		foundTasksInForm.forEach(task -> {
			form.getTasks().forEach(taskForm -> {
				if(taskForm.getId() != null && task.getId() == taskForm.getId()) {
					task.setPrompt(taskForm.getPrompt());
					task.setShuffleOptions(taskForm.isShuffleOptions());
					task.getOptions().clear();
					taskForm.getOptions().forEach(option -> {
						task.getOptions().add(new TaskOption(option.getPrompt(), option.isCorrect()));
					});
				}
			});
		});
	}

	private void removeTasks(Subject subject, List<Task> tasksToBeRemoved) {
		subject.getTasks().removeAll(tasksToBeRemoved);
		taskRepository.deleteAll(tasksToBeRemoved);
	}

	private List<Task> findTasksToBeRemoved(Subject subject, List<Task> foundTasksInForm) {
		List<Task> differences = subject.getTasks().stream()
		        .filter(task -> !foundTasksInForm.contains(task))
		        .collect(Collectors.toList());
		return differences;
	}

	private List<Task> findTasksInForm(SubjectForm form, Subject subject) {
		List<Task> foundTasksInForm = subject.getTasks().stream()
			    .filter(task -> form.getTasks().stream()
			    .anyMatch(taskForm -> taskForm.getId() != null && taskForm.getId() == task.getId()))
		    	.collect(Collectors.toList());
		return foundTasksInForm;
	}
}