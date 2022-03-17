package com.trainingquizzes.english.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Average;
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
public class AverageAppService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<Average> findAveragesByEmail(String email) {
		return userRepository.getAveragesByEmail(email);
	}

	public List<Average> findAveragesById(Long id) {
		return userRepository.getAveragesById(id);
	}
	

}
