package com.trainingquizzes.english.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.QuestsAndTrials;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/test")
public class QuestsAndTrialsTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@GetMapping
	public ResponseEntity<?> test() {
		QuestsAndTrials.generate(userRepository, subjectRepository);
		return ResponseEntity.ok().build();
	}

}
