package com.trainingquizzes.english.api;

import static com.trainingquizzes.english.util.Constants.EMAIL_SET;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.dto.UserDtoNoPassword;
import com.trainingquizzes.english.form.PasswordResetForm;
import com.trainingquizzes.english.form.TokenForm;
import com.trainingquizzes.english.form.UserForm;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.PasswordResetTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.token.PasswordResetToken;
import com.trainingquizzes.english.token.Token;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/api/reset-password")
public class PasswordResetRest {
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;
	
	@Autowired
	private UserRepository userRepository; 
	
	@Value("${spring-english-training-quizzes-default-domain}")
	private String defaultDomain;
	
	@PostMapping("request")
	public ResponseEntity<UserDtoNoPassword> resetPassword(@RequestBody UserForm form) {
		if(form != null) {
			if(userRepository.existsByEmail(form.getEmail())) {
				User user = userRepository.findByEmail(form.getEmail()).orElse(null);
				if(user != null) {
					PasswordResetToken passwordResetToken = createPasswordResetToken(user);
					passwordTokenRepository.save(passwordResetToken);
					sendMail(
							passwordResetToken,
							user.getEmail(), 
							"Complete password reset",
							"To complete the password reset process, please click here: "
									+ defaultDomain
									+ "/#/reset-password?reset_token="
									+ passwordResetToken.getToken()
									+ " (This link will expire after 24 hours)");
					
					return ResponseEntity.ok(new UserDtoNoPassword(user));
				} else {
					return ResponseEntity.badRequest().build();
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("validate-token")
	public ResponseEntity<UserDtoNoPassword> validateResetToken(@RequestBody TokenForm form) {
		if(form != null) {
			PasswordResetToken passwordToken = passwordTokenRepository.findByToken(form.getToken()).orElse(null);
			Date date = new Date();
			if(passwordToken != null && passwordToken.getExpiryDate().after(date)) {
				User user = userRepository.findById(passwordToken.getUserId()).orElse(null);
				
				return ResponseEntity.ok(new UserDtoNoPassword(user));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping
	public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetForm form) {
		if(form != null) {
			try {
				Optional<PasswordResetToken> tokenOptional = passwordTokenRepository.findByToken(form.getToken());
				if(tokenOptional.isPresent()) {
					Optional<User> userOptional = userRepository.findById(tokenOptional.get().getUserId());
					String passwordHashString = BCrypt.withDefaults().hashToString(12, form.getPassword().toCharArray());
					
					if(userOptional.isPresent()) {
						User user = userOptional.get();
						user.setPassword(passwordHashString);
						userRepository.save(user);
						
						return ResponseEntity.ok().build();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				
				return ResponseEntity.badRequest().build();
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
		
	private PasswordResetToken createPasswordResetToken(User user) {
		String token = UUID.randomUUID().toString();
		@SuppressWarnings("deprecation")
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setUserId(user.getId());
		passwordResetToken.setToken(token);
		passwordResetToken.setExpiryDate();
		
		return passwordResetToken;
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
