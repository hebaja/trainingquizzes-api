package com.trainingquizzes.english.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trainingquizzes.english.model.Average;
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
