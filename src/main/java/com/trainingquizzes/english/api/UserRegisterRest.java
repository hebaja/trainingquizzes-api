package com.trainingquizzes.english.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.dto.NewUserRequest;
import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.form.UserForm;
import com.trainingquizzes.english.model.Account;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.model.UserRole;
import com.trainingquizzes.english.repository.PasswordResetTokenRepository;
import com.trainingquizzes.english.repository.UserRegisterTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.token.PasswordResetToken;
import com.trainingquizzes.english.token.Token;
import com.trainingquizzes.english.token.UserRegisterToken;

import static com.trainingquizzes.english.util.Constants.EMAIL_SET;
import static com.trainingquizzes.english.util.Constants.DEFAULT_DOMAIN;

@RestController
@RequestMapping("/api/user/register")
public class UserRegisterRest {
	
	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private UserRegisterTokenRepository registerTokenRepository;
	
	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;
	
	@Autowired
	private JavaMailSender emailSender;
	
	@PostMapping
	public Boolean userRegister(@RequestBody UserForm userForm) {
		
		if (userForm != null) {
			
			User user = userForm.convert();
			
			String token = UUID.randomUUID().toString();
			
			List<UserRole> roles = new ArrayList<>();
			roles.add(new UserRole(Roles.ROLE_USER));
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(AccountType.EMAIL));
			
			UserRegisterToken userToRegisterToken = new UserRegisterToken(token, user.getUsername(), user.getEmail(), user.getPassword());
			userToRegisterToken.setExpiryDate();
			
			registerTokenRepository.save(userToRegisterToken);
			
			sendMail(userToRegisterToken, 
					user.getEmail(), 
					"Complete user registration", 
					"To complete user registration, please click here: "
							+ DEFAULT_DOMAIN
							+ "user/android/confirm-register?token=" 
							+ userToRegisterToken.getToken() 
							+ " (This link will expire after 24 hours).");
			return true;
		}
		return false;
	}
	
	private void sendMail(Token userToken, String emailAddress, String subject, String text) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(emailAddress);
		mailMessage.setSubject(subject);
		mailMessage.setFrom(EMAIL_SET);
		mailMessage.setText(text);
		
		emailSender.send(mailMessage);
	}
	
	@GetMapping("reset-password/{email}")
	private Boolean resetPassowrd(@PathVariable String email) {
		
		System.out.println(email);
		
		Optional<User> userOptional = userRepository.findByEmail(email);
		User user = userOptional.orElse(null);
		System.out.println(user);
		if(user != null) {
			PasswordResetToken passwordResetToken = createPasswordResetToken(user);
			passwordTokenRepository.save(passwordResetToken);
			sendMail(
					passwordResetToken,
					user.getEmail(), 
					"Complete password reset", 
					"To complete the password reset process, please click here: "
							+ DEFAULT_DOMAIN
							+ "user/android/reset-password?id=android&token=" 
							+ passwordResetToken.getToken() 
							+ " (This link will expire after 24 hours)");
			return true;
		}
		return false;
	}
	
	@RequestMapping(value = "confirm-reset", method = {RequestMethod.GET, RequestMethod.POST})
	public String validateResetToken(@RequestParam("token") String confirmationToken) {
		PasswordResetToken passwordToken = passwordTokenRepository.findByToken(confirmationToken);;
		Date date = new Date();
		
		if(passwordToken != null && passwordToken.getExpiryDate().after(date)) {
			Optional<User> userOptional = userRepository.findByEmail(passwordToken.getUser().getEmail());;
			User user = userOptional.get();
			NewUserRequest newUserRequest = new NewUserRequest();
			newUserRequest.setUsername(user.getUsername());
			newUserRequest.setEmail(user.getEmail());
			newUserRequest.setMatchingEmail(user.getEmail());
			return "user/resetPassword";
		} else {
			return "user/error";
		}
	}
	
	private PasswordResetToken createPasswordResetToken(User user) {
		String token = UUID.randomUUID().toString();
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setUser(user);
		passwordResetToken.setToken(token);
		passwordResetToken.setExpiryDate();
		return passwordResetToken;
	}
}