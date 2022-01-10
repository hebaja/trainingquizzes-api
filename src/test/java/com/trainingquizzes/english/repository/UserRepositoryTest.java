package com.trainingquizzes.english.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.trainingquizzes.english.model.Average;
import com.trainingquizzes.english.model.User;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class UserRepositoryTest {

	@Autowired
	private UserRepository repository;
	
	private String username;
	private String adminUsername;
	private String email;
	private String adminEmail;
	private String uid;
	private String adminUid;
	private String inexistentValue;

	@BeforeAll
	public void init() {
		this.username = "user";
		this.adminUsername = "administrator";
		this.email = "henrique@hebaja.com";
		this.adminEmail = "hebaja@hebaja.com";
		this.uid = "845751357545687899";
		this.adminUid = "188445677544887521";
		this.inexistentValue = "null";
	}
	
	@Test
	void shouldReturnUserWhenSearchingByUsername() {
		User user = repository.findByUsername(username).orElse(null);
		assertNotNull(user);
		assertEquals(username, user.getUsername());
	}
	
	@Test
	void shouldReturnNullWhenSearchingForUserWithInexistentUsername() {
		User user = repository.findByUsername(inexistentValue).orElse(null);
		assertNull(user);
	}

	@Test
	void shouldReturnUserWhenSearchingByEmail() {
		User user = repository.findByEmail(email).orElse(null);
		assertNotNull(user);
		assertEquals(email, user.getEmail());
	}
	
	@Test
	void shouldReturnNullWhenSearchingForUserWithInexistentEmail() {
		User user = repository.findByEmail(inexistentValue).orElse(null);
		assertNull(user);
	}
	
	@Test
	void shouldReturnUserWhenSearchingByUid() {
		User user = repository.findByUid(uid).orElse(null);
		assertNotNull(user);
		assertEquals(uid, user.getUid());
	}
	
	@Test
	void shouldReturnNullWhenSearchingForUserWithInexistentUid() {
		User user = repository.findByUid(inexistentValue).orElse(null);
		assertNull(user);
	}
	
	@Test
	void shouldReturnTrueInCaseUserWithGivenEmailExists() {
		assertTrue(repository.existsByEmail(email)); 
	}
	
	@Test
	void shouldReturnListOfAveragesWhenSearchingByUsername() {
		List<Average> averages = repository.getAveragesByUsername(username);
		assertNotNull(averages);
		assertFalse(averages.isEmpty());
	}
	
	@Test
	void shouldReturnEmptyListOfAveragesWhenSearchingByUsername() {
		List<Average> averages = repository.getAveragesByUsername(adminUsername);
		assertNotNull(averages);
		assertTrue(averages.isEmpty());
	}
	
	@Test
	void shouldReturnListOfAveragesWhenSearchingByEmail() {
		List<Average> averages = repository.getAveragesByEmail(email);
		assertNotNull(averages);
		assertFalse(averages.isEmpty());
	}
	
	@Test
	void shouldReturnEmptyListOfAveragesWhenSearchingByEmail() {
		List<Average> averages = repository.getAveragesByEmail(adminEmail);
		assertNotNull(averages);
		assertTrue(averages.isEmpty());
	}
	
	@Test
	void shouldReturnListOfAveragesWhenSearchingByUid() {
		List<Average> averages = repository.getAveragesByUid(uid);
		assertNotNull(averages);
		assertFalse(averages.isEmpty());
	}
	
	@Test
	void shouldReturnEmptyListOfAveragesWhenSearchingByUid() {
		List<Average> averages = repository.getAveragesByUid(adminUid);
		assertNotNull(averages);
		assertTrue(averages.isEmpty());
	}
	
}
