package com.trainingquizzes.english.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Exercise;
import com.trainingquizzes.english.model.ExercisesQuantity;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.User;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
class ExerciseRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	private String userEmail;
	private User user;
	private Subject subject;
	
	@BeforeAll
	public void init() {
		this.userEmail = "henrique@hebaja.com";
		this.user = userRepository.findByEmail(userEmail).orElse(null);
		this.subject = subjectRepository.findById(1L).orElse(null);;
	}
	
	@Test
	void shouldReturnListOfExercisesFromExistingUser() {
		List<Exercise> exercises = exerciseRepository.findAllByUser(user.getUsername());
		assertNotNull(exercises);
		assertFalse(exercises.isEmpty());
	}
	
	@Test
	void shouldReturnListOfExercisesFromExistingUserWithoutExercises() {
		User user = userRepository.findByEmail("hebaja@hebaja.com").orElse(null);
		List<Exercise> exercises = exerciseRepository.findAllByUser(user.getUsername());
		assertNotNull(exercises);
		assertTrue(exercises.isEmpty());
	}
	
	@Test
	void shouldReturnListOfSameExercisesFromExistingUser() {
		List<Exercise> exercises = exerciseRepository.getExercisesByUserLevelAndSubject(user,LevelType.EASY, subject);
		assertNotNull(exercises);
		assertFalse(exercises.isEmpty());
		exercises.forEach(exercise -> {
			assertEquals(exercise.getLevel(), LevelType.EASY);
			assertEquals(exercise.getUser().getId(), user.getId());
			assertEquals(exercise.getSubject().getId(), subject.getId());
		});
	}
	
	@Test
	void shouldReturnQuantityOfTheSameExerciseOfAnExistingUser() {
		ExercisesQuantity exerciseQuantity = exerciseRepository.getQuantityOfTheSameExercise(user, LevelType.EASY, subject).orElse(null);
		assertNotNull(exerciseQuantity);
		assertNotEquals(0, exerciseQuantity.getQuantity());
	}
}
