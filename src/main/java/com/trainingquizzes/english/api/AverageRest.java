package com.trainingquizzes.english.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.dto.AverageDto;
import com.trainingquizzes.english.model.Average;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("/api/averages")
public class AverageRest {
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("email/{email}")
	public List<AverageDto> averagesByEmail(@PathVariable("email") String email) {
		List<Average> averages = new ArrayList<Average>();
		averages = userRepository.getAveragesByEmail(email);
		return AverageDto.convertList(averages);
	}
	
	@GetMapping("uid/{uid}")
	public List<AverageDto> averagesByUid(@PathVariable("uid") String uid) {
		List<Average> averages = new ArrayList<Average>();
		averages = userRepository.getAveragesByUid(uid);
		return AverageDto.convertList(averages);
	}
}