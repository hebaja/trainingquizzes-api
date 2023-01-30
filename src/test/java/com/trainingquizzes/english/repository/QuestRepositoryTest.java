package com.trainingquizzes.english.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import com.trainingquizzes.english.model.Quest;
import com.trainingquizzes.english.model.User;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase
class QuestRepositoryTest {

	@Autowired
	private QuestRepository questRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	void shouldFindQuestById() {
		Optional<Quest> questOptional = questRepository.findById(1L);
		assertTrue(questOptional.isPresent());
	}
	
	@Test
	void shouldFindQuestByUser() {
		Optional<User> userOptional = userRepository.findById(2L);
		Optional<Page<Quest>> optionalQuestPage = questRepository.findByUser(userOptional.get(), null);
		assertFalse(optionalQuestPage.get().isEmpty());
	}
	
	@Test
	void shouldFindBySubscribedUserId() {
		Optional<Page<Quest>> questsOptional = questRepository.findBySubscribedUserId(4L, null);
		assertTrue(questsOptional.isPresent());
	}
	
	@Test
	void shouldFindQuestByPin() {
		Optional<Quest> questOptional = questRepository.findByPin("11111");
		assertTrue(questOptional.isPresent());
	}
	
}
