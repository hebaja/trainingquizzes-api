package com.trainingquizzes.english.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<AverageDto>> averages(@RequestBody AverageRequestForm form) {
		if(form != null) {
			List<Average> averages = service.findAveragesById(form.getId());
			return ResponseEntity.ok(AverageDto.convertList(averages));
		}
		
		return ResponseEntity.badRequest().build();
	}
}