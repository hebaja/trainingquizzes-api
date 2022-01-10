package com.trainingquizzes.english.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.PasswordResetTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
class PasswordResetTokenTest {

	@Autowired
	private PasswordResetTokenRepository tokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private User existingUser;
	
	private PasswordResetToken generatedToken;
	
	@BeforeAll
	void init() {
		Optional<User> userOptional = userRepository.findByEmail("henrique@hebaja.com");
		this.existingUser = userOptional.orElse(null);
		
		String token = UUID.randomUUID().toString();
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setUser(existingUser);
		passwordResetToken.setToken(token);
		passwordResetToken.setExpiryDate();
		
		this.generatedToken = tokenRepository.save(passwordResetToken);;
		
	}
	
	@Test
	void shouldChangeExistingUserPasswordBasedOnTokenAndDeleteThisToken() {
		Date date = new Date();
		String newPassword = "456123";
		String passwordHashString = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());

		if(generatedToken.getExpiryDate().after(date)) {
			existingUser.setPassword(passwordHashString);
			userRepository.save(existingUser);
			tokenRepository.delete(generatedToken);
		}
		
		PasswordResetToken deletedToken = tokenRepository.findById(generatedToken.getId()).orElse(null);
		
		assertNotNull(existingUser);
		assertNull(deletedToken);
		assertEquals(passwordHashString, existingUser.getPassword());
		
	}

}
