package com.trainingquizzes.english.api;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.form.ExerciseForm;
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
	
	// RETURNING INFINITE RECURSION
	@GetMapping("user/{username}")
	List<Exercise> getExercises(@PathVariable("username") String username) {
		return exerciseRepository.findAllByUser(username);
	}
	
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody List<ExerciseForm> exerciseFormList, UriComponentsBuilder uriBuilder) {
		
		String userUid = exerciseFormList.get(0).getUserUid();
		
		if(userUid != null) {
			User user = userRepository.findByUid(userUid).orElse(null);

			if(user != null) {
				exerciseFormList.forEach(exerciseForm -> {
					
					LevelType level = exerciseForm.getLevel();
					Long subjectId = exerciseForm.getSubjectId();
					
					Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
					Subject subject = subjectOptional.orElse(null);
					
					Optional<ExercisesQuantity> exerciseQuantityOptional = exerciseRepository.getQuantityOfTheSameExercise(user, level, subject);
					
					exercisesQuantity = exerciseQuantityOptional.orElse(null);
					
					if(exercisesQuantity == null) {
						exercisesQuantity = new ExercisesQuantity(user, level, subject, 0);
					}
					
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
//				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User was not found");
				return ResponseEntity.notFound().build();
			}
		} else {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User uid was not found");
			return ResponseEntity.notFound().build();
		}

	}
}