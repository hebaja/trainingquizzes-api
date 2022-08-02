package com.trainingquizzes.english.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.dto.TrialDto;
import com.trainingquizzes.english.form.SaveTemporaryTrialDataStore;
import com.trainingquizzes.english.model.TemporaryTrialDataStore;
import com.trainingquizzes.english.model.Trial;
import com.trainingquizzes.english.repository.TemporaryTrialDataStoreRepository;
import com.trainingquizzes.english.repository.TrialRepository;

@RestController
@CrossOrigin
@RequestMapping("api/trial")
public class TrialRest {
	
	@Autowired
	private TemporaryTrialDataStoreRepository temporaryTrialDataStoreRepository;
	
	@Autowired
	private TrialRepository trialRepository;
	
	@PostMapping("save")
	public ResponseEntity<TrialDto> saveTrial(@RequestBody SaveTemporaryTrialDataStore form) {
		if(form != null) {
			Optional<TemporaryTrialDataStore> optionalTrialItem = temporaryTrialDataStoreRepository.findById(form.getId());
			if(optionalTrialItem.isPresent()) {
				TemporaryTrialDataStore temporaryTrialDataStore = optionalTrialItem.get();
				if(!temporaryTrialDataStore.isFinished()) {
					temporaryTrialDataStore.setFinished(form.isFinished());
					if(form.isCorrect()) {
						temporaryTrialDataStore.setScore(temporaryTrialDataStore.getScore() + 1);
					}
					temporaryTrialDataStore.iterateTasksIndex();
					TemporaryTrialDataStore savedTemporaryTrialDataStore = temporaryTrialDataStoreRepository.save(temporaryTrialDataStore);
					Trial trial = savedTemporaryTrialDataStore.getTrial();
					trial.setScore(savedTemporaryTrialDataStore.getScore());
					trial.setFinished(savedTemporaryTrialDataStore.isFinished());
					Trial savedTrial = trialRepository.save(trial);
					
					return ResponseEntity.ok(new TrialDto(savedTrial));
				} else {
					return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
				}
			}
		}
		
		return ResponseEntity.badRequest().build();
	}

}
