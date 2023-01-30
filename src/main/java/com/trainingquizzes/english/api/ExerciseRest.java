package com.trainingquizzes.english.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.trainingquizzes.english.dto.ExerciseDto;
import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.form.ExerciseForm;
import com.trainingquizzes.english.form.ResultForm;
import com.trainingquizzes.english.model.Exercise;
import com.trainingquizzes.english.model.ExercisesQuantity;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.ExerciseRepository;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseRest {
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	private ExercisesQuantity exercisesQuantity;
	
	@PostMapping("save")
	public ResponseEntity<ExerciseDto> saveResult(@RequestBody ResultForm form) {
		if(form != null) {
			Optional<User> userOptional = userRepository.findById(form.getUserId());
			Optional<Subject> subjectOptional = subjectRepository.findById(form.getSubjectId());
			if(userOptional.isPresent() && subjectOptional.isPresent()) {
				Exercise exercise = buildAndSaveExercises(userOptional.get(), subjectOptional.get(), form.getScore());
				
				return ResponseEntity.ok(ExerciseDto.convert(exercise));
			} 
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("save-results")
	public ResponseEntity<List<ExerciseDto>> saveListResult(@RequestBody List<ExerciseForm> form) {
		if(form != null) {
			User user = userRepository.findById(form.get(0).getUserId()).orElse(null);
			if(user != null) {
				List<Exercise> savedExercises = new ArrayList<>();
				form.forEach(exerciseForm -> {
					Subject subject = subjectRepository.findById(exerciseForm.getSubjectId()).orElse(null);
					Exercise exercise = buildAndSaveExercises(user, subject, exerciseForm.getScore());
					savedExercises.add(exercise);
				});
				
				return ResponseEntity.ok(ExerciseDto.convertList(savedExercises));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody List<ExerciseForm> exerciseFormList, UriComponentsBuilder uriBuilder) {
		Long userId = exerciseFormList.get(0).getUserId();
		if(userId != null) {
			User user = userRepository.findById(userId).orElse(null);
			if(user != null) {
				exerciseFormList.forEach(exerciseForm -> {
					LevelType level = exerciseForm.getLevel();
					Long subjectId = exerciseForm.getSubjectId();
					Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
					Subject subject = subjectOptional.orElse(null);
					Optional<ExercisesQuantity> exerciseQuantityOptional = exerciseRepository.getQuantityOfTheSameExercise(user, level, subject);
					exercisesQuantity = exerciseQuantityOptional.orElse(null);
					if(exercisesQuantity == null) exercisesQuantity = new ExercisesQuantity(user, level, subject, 0);
					if(exercisesQuantity.getQuantity() >= 10) {
						List<Exercise> exercisesList = exerciseRepository.getExercisesByUserLevelAndSubject(user, level, subject);
						Exercise exerciseToBeRemoved = exercisesList.get(0);
						exerciseRepository.delete(exerciseToBeRemoved);
					}
					Exercise exercise = exerciseForm.convert(user, subject);
					exerciseRepository.save(exercise);
				});
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}

	}
	
	private Exercise buildAndSaveExercises(User user, Subject subject, double score) {
		Exercise exercise = new Exercise(user, subject, subject.getLevel(), score);
		ExercisesQuantity exercisesQuantityCreated = exerciseRepository.getQuantityOfTheSameExercise(user, exercise.getLevel(), exercise.getSubject()).orElse(null);
		if (exercisesQuantityCreated == null) {
			exercisesQuantityCreated = new ExercisesQuantity(user, exercise.getLevel(), exercise.getSubject(), 0);
		}
		removeOlderExercise(exercisesQuantityCreated, user, exercise);
		
		return exerciseRepository.save(exercise);
	}
	
	private void removeOlderExercise(ExercisesQuantity exercisesQuantity, User user, Exercise exercise) {
		if(exercisesQuantity.getQuantity() >= 10) {
			List<Exercise> exerciseList = exerciseRepository.getExercisesByUserLevelAndSubject(user,exercise.getLevel(), exercise.getSubject());
			Exercise exerciseToBeDeleted = exerciseList.get(0);
			exerciseRepository.delete(exerciseToBeDeleted);
		}
	}
}