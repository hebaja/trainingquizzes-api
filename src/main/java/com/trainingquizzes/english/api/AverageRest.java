package com.trainingquizzes.english.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.model.Average;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("/api/averages")
public class AverageRest {
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("email/{email}")
	public List<Average> averagesByEmail(@PathVariable("email") String email) {
		List<Average> resultList = new ArrayList<Average>();
		resultList = userRepository.getAveragesByEmail(email);
		
		return resultList;
	}
	
	@GetMapping("uid/{uid}")
	public List<Average> averagesByUid(@PathVariable("uid") String uid) {
		List<Average> resultList = new ArrayList<Average>();
		resultList = userRepository.getAveragesByUid(uid);
		System.out.println(resultList);
		
		return resultList;
	}
}