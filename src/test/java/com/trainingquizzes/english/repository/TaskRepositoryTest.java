package com.trainingquizzes.english.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Task;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase
class TaskRepositoryTest {

	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	private Subject subject;
	
	@BeforeAll
	void init() {
		subject = subjectRepository.findById(1L).orElse(null);
	}
	
	@Test
	void shouldReturnListOfTasksFromExistingSubject() {
		List<Task> tasks = taskRepository.findAllBySubject(subject).orElse(null);
		assertNotNull(tasks);
		assertFalse(tasks.isEmpty());
	}

}
