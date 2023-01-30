package com.trainingquizzes.english.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.trainingquizzes.english.dto.SubjectDto;
import com.trainingquizzes.english.dto.SubjectWithTasksDto;
import com.trainingquizzes.english.dto.UserWithSubjectDto;
import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.form.SubjectForm;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.model.TaskOption;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.QuestRepository;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.TaskRepository;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("/api/subject")
public class SubjectRest {

	private static final String USER_WITH_SUBJECT_FIREBASE_MESSAGE = "user_with_subject";
	private static final String SUBJECT_REMOVED_FIREBASE_MESSAGE = "subject_removed";

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private QuestRepository questRepository;
	
	Logger logger = LoggerFactory.getLogger(SubjectRest.class);
	
	@GetMapping
	@Cacheable(value = "subject")
	public ResponseEntity<SubjectWithTasksDto> subjectById(@RequestParam("subjectId") Long subjectId) {
		Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
		Subject subject = subjectOptional.orElse(null);
		if (subject != null) {
			SubjectWithTasksDto subjectWithTaskDto = SubjectWithTasksDto.convertFromSubject(subject);
			
			return ResponseEntity.ok(subjectWithTaskDto);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("all")
	@Cacheable(value = "subjectsList")
	public ResponseEntity<List<SubjectWithTasksDto>> subjects() {
		List<Subject> subjects = subjectRepository.findAll();
		
		return ResponseEntity.ok(SubjectWithTasksDto.convertList(subjects));
	}
	
	@GetMapping("level")
	public ResponseEntity<List<SubjectDto>> subjects(@RequestParam("level") LevelType level) {
		if(level != null) {
			Optional<List<Subject>> subjectsOptional = subjectRepository.findAllByLevel(level);
			if(subjectsOptional.isPresent()) {
				return ResponseEntity.ok(SubjectDto.convertList(subjectsOptional.get()))  ;
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("user")
	public ResponseEntity<Page<SubjectDto>> subjectsUser(@RequestParam("userId") Long userId, @RequestParam(name = "level", required = false) String level, Pageable pageable) {
		if(userId != null) {
			if(level != null) {
				LevelType levelType = null;
				switch(level) {
					case "easy":
						levelType = LevelType.EASY;
						break;
					case "medium":
						levelType = LevelType.MEDIUM;
						break;
					case "hard":
						levelType = LevelType.HARD;
						break;
					default:
						levelType = LevelType.EASY;
				}
				Page<Subject> subjects = subjectRepository.findPageableByUserIdAndLevel(userId, levelType, pageable);
				
				return ResponseEntity.ok(SubjectDto.convertToPageable(subjects));
			}
			Page<Subject> subjects = subjectRepository.findPageableByuserId(userId, pageable);
			
			return ResponseEntity.ok(SubjectDto.convertToPageable(subjects));
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("reduced-list")
	public ResponseEntity<Page<SubjectDto>> subjectsReducedList(@RequestParam(name = "query", required = false) String query, @RequestParam(required = false) Long userId, Pageable pagination) {
		if(query != null) {
			if(userId != null) {
				Optional<User> userOptional = userRepository.findById(userId);
				if(userOptional.isPresent()) {
					String searchQuery = "%" + query + "%"; 
					Page<Subject> subjects = subjectRepository.findByTitleLikeIgnoreCaseAndUser(searchQuery, userOptional.get(), pagination);
					return ResponseEntity.ok(SubjectDto.convertToPageable(subjects));
				}
				
				return ResponseEntity.badRequest().build();
			} else {
				String searchQuery = "%" + query + "%"; 
				Page<Subject> subjects = subjectRepository.findByTitleLikeIgnoreCase(searchQuery, pagination);
				
				return ResponseEntity.ok(SubjectDto.convertToPageable(subjects));
			}
		} else {
			Page<Subject> subjects = subjectRepository.findAll(pagination);
			
			return ResponseEntity.ok(SubjectDto.convertToPageable(subjects));
		}
	}
	
	@GetMapping("pageable-teacher")
	public ResponseEntity<Page<SubjectDto>> pageableSubjectsByTeacher(@RequestParam Long userId, Pageable pagination) {
		if(userId != null) {
			Optional<User> userOptional = userRepository.findById(userId);
			if(userOptional.isPresent()) {
				Page<Subject> subjects = subjectRepository.findAllByUser(userOptional.get(), pagination);
				return ResponseEntity.ok(SubjectDto.convertToPageable(subjects));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("teacher")
	public ResponseEntity<List<SubjectWithTasksDto>> subjectsByTeacher(@RequestParam Long id) {
		if(id != null) {
			Optional<List<Subject>> subjectsOptional = subjectRepository.findAllByUserId(id);
			
			if(subjectsOptional.isPresent() && !subjectsOptional.get().isEmpty()) {
				return ResponseEntity.ok(SubjectWithTasksDto.convertList(subjectsOptional.get()));
			}
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("teacher/reduced-list")
	public ResponseEntity<List<SubjectDto>> subjectsByTeacherReducedList(@RequestParam Long id) {
		if(id != null) {
			Optional<List<Subject>> subjectsOptional = subjectRepository.findAllByUserId(id);
			
			if(subjectsOptional.isPresent() && !subjectsOptional.get().isEmpty()) {
				return ResponseEntity.ok(SubjectDto.convertList(subjectsOptional.get()));
			}
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.badRequest().build();
	}

	@DeleteMapping
	@CacheEvict(value = {"subject", "subjectsList", "userWithSubjectsList"}, allEntries = true)
	public ResponseEntity<List<SubjectWithTasksDto>> delete(@RequestParam("subjectId") Long subjectId) {
		Optional<Subject> subjectToBeRemovedOptional = subjectRepository.findById(subjectId);
		if(subjectToBeRemovedOptional.isPresent()) {
			questRepository.deleteAllBySubject(subjectToBeRemovedOptional.get());
			subjectRepository.delete(subjectToBeRemovedOptional.get());
			List<Subject> subjects = subjectRepository.findAllByUser(subjectToBeRemovedOptional.get().getUser()).orElse(null);
			
			return ResponseEntity.ok(SubjectWithTasksDto.convertList(subjects));
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PutMapping
	@CacheEvict(value = {"subject", "subjectsList"}, allEntries = true)
	public ResponseEntity<List<SubjectWithTasksDto>> update(@RequestBody SubjectForm form) {
		
		Subject subject = null;
		User user = userRepository.findById(form.getUser().getId()).orElse(null);
		
		if(form.getId() != null && form.getId() > 0) {
			subject = subjectRepository.findById(form.getId()).orElse(null);
		} else {
			subject = new Subject();
			subject.setTasks(new ArrayList<Task>());
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
			Optional<List<Subject>> optionalSubjects = subjectRepository.findAllByUser(user);
			
			if(optionalSubjects.isPresent()) {
				
				return ResponseEntity.ok(SubjectWithTasksDto.convertList(optionalSubjects.get()));
			}
		}
		return ResponseEntity.badRequest().build();
	}

	private void createNewTasks(SubjectForm form, Subject subject) {
		form.getTasks().forEach(taskForm -> {
			if(taskForm.getId() == null || taskForm.getId() == 0) {
				Task task = new Task();
				task.setPrompt(taskForm.getPrompt());
				task.setShuffleOptions(taskForm.isShuffleOptions());
				task.setSubject(subject);
				
				List<TaskOption> options = new ArrayList<>();
				
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
		return subject.getTasks().stream()
		        .filter(task -> !foundTasksInForm.contains(task))
		        .collect(Collectors.toList());
	}

	private List<Task> findTasksInForm(SubjectForm form, Subject subject) {
		return subject.getTasks().stream()
			    .filter(task -> form.getTasks().stream()
			    .anyMatch(taskForm -> taskForm.getId() != null && taskForm.getId() == task.getId()))
		    	.collect(Collectors.toList());
	}
	
}