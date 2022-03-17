package com.trainingquizzes.english.api;

import static com.trainingquizzes.english.util.Constants.EMAIL_SET;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.dto.NewUserRequest;
import com.trainingquizzes.english.dto.UserDto;
import com.trainingquizzes.english.dto.UserDtoNoPassword;
import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.form.PasswordResetForm;
import com.trainingquizzes.english.form.TokenForm;
import com.trainingquizzes.english.form.UserForm;
import com.trainingquizzes.english.model.Account;
import com.trainingquizzes.english.model.Authority;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.model.UserRole;
import com.trainingquizzes.english.repository.PasswordResetTokenRepository;
import com.trainingquizzes.english.repository.UserRegisterTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.token.PasswordResetToken;
import com.trainingquizzes.english.token.Token;
import com.trainingquizzes.english.token.UserRegisterToken;

@RestController
@RequestMapping("/api/user-register")
public class UserRegisterRest {
	
	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private UserRegisterTokenRepository registerTokenRepository;
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Value("${spring-english-training-quizzes-default-domain}")
	private String defaultDomain;
	
	@PostMapping
	public ResponseEntity<UserDto> userRegister(@RequestBody UserForm userForm) {
		if (userForm != null) {
			if(!userRepository.existsByEmail(userForm.getEmail())) {
				String token = UUID.randomUUID().toString();
				User user = buildUser(userForm);
				UserRegisterToken userToRegisterToken = buildToken(token, user);
				buildEmail(user, userToRegisterToken, "#/signin?register_token=");
				return ResponseEntity.ok(new UserDto(user));
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("android")
	public ResponseEntity<UserDto> userRegisterAndroid(@RequestBody UserForm userForm) {
		if (userForm != null) {
			if(!userRepository.existsByEmail(userForm.getEmail())) {
				String token = UUID.randomUUID().toString();
				User user = buildUser(userForm);
				UserRegisterToken userToRegisterToken = buildToken(token, user);
				buildEmail(user, userToRegisterToken, "#/android/confirm_register?register_token=");
				return ResponseEntity.ok(new UserDto(user));
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("confirm")
	public ResponseEntity<UserDtoNoPassword> confirmRegister(@RequestBody TokenForm form) throws IOException {
		String registerToken = form.getToken();
		if(registerToken != null) {
									
			UserRegisterToken userRegisterToken = registerTokenRepository.findByToken(registerToken);
			Date date = new Date();
			
			if(userRegisterToken != null && userRegisterToken.getExpiryDate().after(date)) {
				User newUser = createUser(userRegisterToken);
				try {
					User savedUser = saveUser(userRegisterToken, newUser);
					return ResponseEntity.ok(new UserDtoNoPassword(savedUser));
				} catch (Exception e) {
					System.out.println("printing stack");
					e.printStackTrace();
					return ResponseEntity.badRequest().build();
				}
			} 
		}
		System.out.println("problem");
		return ResponseEntity.badRequest().build();
	}
	
	private User createUser(UserRegisterToken userRegisterToken) throws IOException {
		List<Account> accounts = new ArrayList<>();
		accounts.add(new Account(AccountType.EMAIL));
		
		Authority authority = new Authority(Roles.ROLE_USER);
		List<Authority> roles = Arrays.asList(authority);
		
		String uid = RandomStringUtils.random(18, "0123456789");
		
		User newUser = new User(userRegisterToken.getUsername(), userRegisterToken.getEmail(),
				userRegisterToken.getPasword(), true, roles, accounts);
		
		newUser.setUid(uid);
		return newUser;
	}
	
	private User saveUser(UserRegisterToken userRegisterToken, User newUser) {
		User user = userRepository.save(newUser);
		registerTokenRepository.delete(userRegisterToken);
		return user;
	}
	
	private void buildEmail(User user, UserRegisterToken userToRegisterToken, String url) {
		sendMail(userToRegisterToken, 
				user.getEmail(), 
				"Complete user registration", 
				"To complete user registration, please click here: "
						+ defaultDomain
						+ url
						+ userToRegisterToken.getToken() 
						+ " (This link will expire after 24 hours).");
	}

	private UserRegisterToken buildToken(String token, User user) {
		UserRegisterToken userToRegisterToken = new UserRegisterToken(token, user.getUsername(), user.getEmail(), user.getPassword());
		userToRegisterToken.setExpiryDate();
		registerTokenRepository.save(userToRegisterToken);
		return userToRegisterToken;
	}

	private User buildUser(UserForm userForm) {
		User user = userForm.convert();
		List<UserRole> roles = new ArrayList<>();
		roles.add(new UserRole(Roles.ROLE_USER));
		List<Account> accounts = new ArrayList<>();
		accounts.add(new Account(AccountType.EMAIL));
		user.setAccounts(accounts);
		return user;
	}
	
	private void sendMail(Token userToken, String emailAddress, String subject, String text) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(emailAddress);
		mailMessage.setSubject(subject);
		mailMessage.setFrom(EMAIL_SET);
		mailMessage.setText(text);
		
		emailSender.send(mailMessage);
	}
	
}