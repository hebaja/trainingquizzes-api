package com.trainingquizzes.english.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.dto.AverageDto;
import com.trainingquizzes.english.form.AverageRequestForm;
import com.trainingquizzes.english.model.Average;
import com.trainingquizzes.english.service.AverageAppService;

@RestController
@CrossOrigin
@RequestMapping("/api/averages")
public class AverageRest {

	@Autowired
	private AverageAppService service;
	
	@PostMapping
	public List<AverageDto> averages(@RequestBody AverageRequestForm form) {
		List<Average> averages = service.findAveragesById(form.getId());
		return AverageDto.convertList(averages);
	}
}