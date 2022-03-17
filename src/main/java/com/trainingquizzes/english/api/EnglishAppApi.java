package com.trainingquizzes.english.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.dto.ExerciseDto;
import com.trainingquizzes.english.dto.SubjectDto;
import com.trainingquizzes.english.dto.TaskDto;
import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.form.ExerciseForm;
import com.trainingquizzes.english.form.ResultForm;
import com.trainingquizzes.english.form.ResultListForm;
import com.trainingquizzes.english.model.Exercise;
import com.trainingquizzes.english.model.ExercisesQuantity;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.service.EnglishAppService;

@RestController
@RequestMapping("api/english")
public class EnglishAppApi {
	
	@Autowired
	private EnglishAppService service;
		
	@GetMapping("subjects{level}")
	public List<SubjectDto> subjects(@RequestParam("level") LevelType level) {
		return SubjectDto.convertList(service.findSubjectsByLevel(level));
	}
	
	@GetMapping("task{subjectId}")
	public List<TaskDto> tasks(@RequestParam("subjectId") Long subjectId) {
		return TaskDto.convertList(service.findTasksBySubjectId(subjectId));
	}
	
	@PostMapping("save-result")
	public ResponseEntity<ExerciseDto> saveResult(@RequestBody ResultForm form) {

		User user = service.findUserById(form.getUserId());
		Subject subject = service.findSubjectById(form.getSubjectId());
				
		if(user != null && subject != null) {
			Exercise exercise = buildAndSaveExercises(user, subject, form.getScore());
			return ResponseEntity.ok(ExerciseDto.convert(exercise));
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("save-list-results")
	public ResponseEntity<List<ExerciseDto>> saveListResult(@RequestBody List<ExerciseForm> form) {
		if(form != null) {
			User user = service.findUserById(form.get(0).getUserId());
			if(user != null) {
				List<Exercise> savedExercises = new ArrayList<Exercise>();
				form.forEach(exerciseForm -> {
					Subject subject = service.findSubjectById(exerciseForm.getSubjectId());
					Exercise exercise = buildAndSaveExercises(user, subject, exerciseForm.getScore());
					savedExercises.add(exercise);
				});
				return ResponseEntity.ok(ExerciseDto.convertList(savedExercises));
			}
		}
		return ResponseEntity.badRequest().build();
	}
	
	private Exercise buildAndSaveExercises(User user, Subject subject, double score) {
		Exercise exercise = new Exercise(user, subject, subject.getLevel(), score);
		ExercisesQuantity exercisesQuantity = service.findQuantityOfTheSameExercise(user, exercise.getLevel(), exercise.getSubject());
		
		if (exercisesQuantity == null) {
			exercisesQuantity = new ExercisesQuantity(user, exercise.getLevel(), exercise.getSubject(), 0);
		}
		
		removeOlderExercise(exercisesQuantity, user, exercise);
		Exercise savedExercise = service.save(exercise);
		return savedExercise;
	}
	
	private void removeOlderExercise(ExercisesQuantity exercisesQuantity, User user, Exercise exercise) {
		if(exercisesQuantity.getQuantity() >= 10) {
			List<Exercise> exerciseList = service.findExercisesByUserLevelAndSubject(user,exercise.getLevel(), exercise.getSubject());
			Exercise exerciseToBeDeleted = exerciseList.get(0);
			service.delete(exerciseToBeDeleted);
		}
	}
	
}
