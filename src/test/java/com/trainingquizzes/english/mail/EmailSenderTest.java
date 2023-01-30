package com.trainingquizzes.english.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.model.Account;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.model.UserRole;
import com.trainingquizzes.english.token.UserRegisterToken;

@ActiveProfiles("test")
public class EmailSenderTest {
	
	@Mock
	private JavaMailSender emailSender;
	
	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void shouldSendEmailToUserEmailWhenRegisterIsRequested() {
		
		User user = new User();
		String token = UUID.randomUUID().toString();
		
		List<UserRole> roles = new ArrayList<>();
		roles.add(new UserRole(Roles.ROLE_TEACHER));
		
		List<Account> accounts = new ArrayList<>();
		accounts.add(new Account(AccountType.EMAIL));
		
		UserRegisterToken userToRegisterToken = new UserRegisterToken(token, user.getUsername(), user.getEmail(), user.getPassword(), Roles.ROLE_TEACHER);
		userToRegisterToken.setExpiryDate();
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		mailMessage.setTo("someemail@email.com");
		mailMessage.setSubject("subject");
		mailMessage.setFrom("serveremail@email.com");
		mailMessage.setText("sometext");
		
		emailSender.send(mailMessage);
		
	}

}
