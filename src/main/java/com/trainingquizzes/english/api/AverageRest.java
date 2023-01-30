package com.trainingquizzes.english.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.dto.AverageDto;
import com.trainingquizzes.english.form.AverageRequestForm;
import com.trainingquizzes.english.model.Average;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("/api/averages")
public class AverageRest {

	@Autowired
	private UserRepository repository;
	
	@PostMapping
	public ResponseEntity<List<AverageDto>> averages(@RequestBody AverageRequestForm form) {
		if(form != null) {
			List<Average> averages = repository.getAveragesById(form.getId());
			return ResponseEntity.ok(AverageDto.convertList(averages));
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping
	public ResponseEntity<Page<AverageDto>> pageableAverages(@RequestParam("userId") Long userId, Pageable pageagle) {
		if(userId != null) {
			Optional<Page<Average>> averagesOptional = repository.findPageableAveragesByUserId(userId, pageagle);
			if(averagesOptional.isPresent()) return ResponseEntity.ok(AverageDto.convertToPageable(averagesOptional.get()));
		}
		
		return ResponseEntity.badRequest().build();
	}
}