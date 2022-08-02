package com.trainingquizzes.english.rules;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.trainingquizzes.english.model.Exercise;
import com.trainingquizzes.english.model.ExercisesQuantity;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.ExerciseRepository;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class ExerciseRulesTest {

	@Autowired
	private ExerciseRepository exerciseRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	private List<Exercise> exercisesToSave;
	private User user;
	private Subject subject;
	
	@BeforeAll
	void init() {
		this.user = userRepository.findById(4L).orElse(null);
		this.subject = subjectRepository.findById(1L).orElse(null);
		this.exercisesToSave = Arrays.asList(new Exercise(user, subject, subject.getLevel(), 7.0), new Exercise(user, subject, subject.getLevel(), 6.0));
	}
	
	@Test
	void shouldReturnCorrectQuantityOfExecisesInDatabase() {
		assertNotNull(user);
		assertNotNull(subject);
		ExercisesQuantity exerciseQuantity = exerciseRepository.getQuantityOfTheSameExercise(user, subject.getLevel(), subject).orElse(null);
		assertNotNull(exerciseQuantity);
		assertEquals(9L, exerciseQuantity.getQuantity());
	}
	
	@Test
	void shouldRemoveOlderExerciseInDatabaseCaseExerciseQuantityIsGreaterThan10() {
		exerciseRepository.saveAll(exercisesToSave);
		ExercisesQuantity exerciseQuantity = exerciseRepository.getQuantityOfTheSameExercise(user, subject.getLevel(), subject).orElse(null);
		assertEquals(exerciseQuantity.getQuantity(), 11L);
		List<Exercise> exercisesList = exerciseRepository.getExercisesByUserLevelAndSubject(user, subject.getLevel(), subject);
		Exercise exerciseToBeRemoved = exercisesList.get(0);
		exerciseRepository.delete(exerciseToBeRemoved);
		ExercisesQuantity reducedExerciseQuantity = exerciseRepository.getQuantityOfTheSameExercise(user, subject.getLevel(), subject).orElse(null);
		assertEquals(reducedExerciseQuantity.getQuantity(), 10L);
	}
	
}
