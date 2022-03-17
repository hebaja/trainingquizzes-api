package com.trainingquizzes.english.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Exercise;
import com.trainingquizzes.english.model.ExercisesQuantity;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.ExerciseRepository;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.TaskRepository;
import com.trainingquizzes.english.repository.UserRepository;

@Service
public class EnglishAppService {
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	public List<Subject> findSubjects() {
		return subjectRepository.findAll();
	}
	
	public List<Subject> findSubjectsByLevel(LevelType level) {
		return subjectRepository.findAllByLevel(level).orElse(null);
	}
	
	public List<Task> findTasksBySubjectId(Long id) {
		return taskRepository.findAllBySubjectId(id).orElse(null);
	}
	
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}
	
	public User findUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public Subject findSubjectById(Long id) {
		return subjectRepository.findById(id).orElse(null);
	}

	public ExercisesQuantity findQuantityOfTheSameExercise(User user, LevelType level, Subject subject) {
		return exerciseRepository.getQuantityOfTheSameExercise(user, level, subject).orElse(null);
	}

	public List<Exercise> findExercisesByUserLevelAndSubject(User user, LevelType level, Subject subject) {
		return exerciseRepository.getExercisesByUserLevelAndSubject(user, level, subject);
	}

	public Exercise save(Exercise exercise) {
		return exerciseRepository.save(exercise);
	}

	public void delete(Exercise exercise) {
		exerciseRepository.delete(exercise);
	}
	
	

}
