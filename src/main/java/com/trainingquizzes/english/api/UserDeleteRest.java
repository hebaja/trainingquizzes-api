package com.trainingquizzes.english.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.PasswordResetTokenRepository;
import com.trainingquizzes.english.repository.UserRegisterTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.token.PasswordResetToken;
import com.trainingquizzes.english.token.UserRegisterToken;

@RestController
@CrossOrigin
@RequestMapping("/api/delete-user")
// PROBABLY NOT GOING TO USE THIS CLASS ANYMORE
public class UserDeleteRest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;
	
	@Autowired
	private UserRegisterTokenRepository userRegisterTokenRepository;
	
	@DeleteMapping
	public ResponseEntity<?> delete(@RequestParam Long userId) {
		
		User user = fetchUser(userId);
		if(user != null) {
			try {
				removeUser(user);
				return ResponseEntity.ok().build();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return ResponseEntity.badRequest().build();
	}

	private User fetchUser(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}
	
	private void removeUser(User user) {
		List<PasswordResetToken> passwordResetTokens = passwordTokenRepository.findAllByTeacherId(user.getId());
		if(passwordResetTokens != null) {
			passwordTokenRepository.deleteAll(passwordResetTokens);
		}
		
		List<UserRegisterToken> userRegisterTokens = userRegisterTokenRepository.findAllByEmail(user.getEmail());
		if(userRegisterTokens != null) {
			userRegisterTokenRepository.deleteAll(userRegisterTokens);
		}
		
		userRepository.delete(user);
	}

}
