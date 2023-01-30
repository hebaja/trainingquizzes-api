package com.trainingquizzes.english.token;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.model.Account;
import com.trainingquizzes.english.model.Authority;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.model.UserRole;
import com.trainingquizzes.english.repository.UserRegisterTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase
class UserRegisterTokenTest {

	@Autowired
	private UserRegisterTokenRepository tokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private UserRegisterToken generatedToken;
	
	@BeforeAll
	void init() {
		String token = UUID.randomUUID().toString();
		
		User user = new User();
		user.setUsername("test");
		user.setEmail("test@hebaja.com");
		user.setPassword("123456");
		
		List<UserRole> roles = new ArrayList<>();
		roles.add(new UserRole(Roles.ROLE_TEACHER));
		
		List<Account> accounts = new ArrayList<>();
		accounts.add(new Account(AccountType.EMAIL));
		
		String passwordHashString = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
		
		UserRegisterToken userToRegisterToken = new UserRegisterToken(token, user.getUsername(), user.getEmail(), user.getPassword(), Roles.ROLE_TEACHER);
		userToRegisterToken.setExpiryDate();
		
		this.generatedToken = tokenRepository.save(userToRegisterToken);
		
	}
	
	@Test
	void shouldSaveNewUserBasedOnTokenAndDeleteUsedToken() {
		Date date = new Date();
		
		if(generatedToken.getExpiryDate().after(date)) {
			Set<Account> accounts = new HashSet<>();
			accounts.add(new Account(AccountType.EMAIL));
			
			Set<Authority> roles = new HashSet<>();
			roles.add(new Authority(Roles.ROLE_TEACHER));
			
			String uid = RandomStringUtils.random(18, "0123456789");
			
			User user = new User(generatedToken.getUsername(), generatedToken.getEmail(),
					generatedToken.getPasword(), true, roles, accounts);
			
			user.setUid(uid);
			
			User generatedUser = userRepository.save(user);;
			tokenRepository.delete(generatedToken);
			UserRegisterToken removedToken = tokenRepository.findById(generatedToken.getId()).orElse(null);
			
			assertNotNull(generatedUser);
			assertNull(removedToken);
			
		}
		
	}

}
