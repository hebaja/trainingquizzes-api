package com.trainingquizzes.english.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingquizzes.english.form.UserForm;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.PasswordResetTokenRepository;
import com.trainingquizzes.english.repository.UserRegisterTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.token.PasswordResetToken;
import com.trainingquizzes.english.token.UserRegisterToken;

@RestController
@RequestMapping("/api/delete-user")
public class UserDeleteRest {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;
	
	@Autowired
	private UserRegisterTokenRepository userRegisterTokenRepository;
	
	@PostMapping
	public ResponseEntity<?> delete(@RequestBody UserForm form) {
		
		User user = fetchUser(form.getEmail());
		if(user != null) {
			try {
				removeUser(user);
				return ResponseEntity.ok().build();
			} catch (Exception e) {
				return ResponseEntity.badRequest().build();
			}
		}
		return ResponseEntity.badRequest().build();
	}

//	public void androidDelete() throws IOException {
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		User user = fetchUser();
//		if(user != null) {
//			removeUser(user);
//			removedUserMessage(facesContext, user);
//			setReturnToAppRender(true);
//			setDeleteAccountButtonRender(false);
//		} else {
//			showErrorMessage(facesContext);
//		}
//	}
	
	private User fetchUser(String email) {
		
		return repository.findByEmail(email).orElse(null);
		
//		User user = userRepository.findByEmail(email)
//						.orElse(userRepository.findByUid(userInfo)
//						.orElse(null);
//		return user;
	}
	
	private void removeUser(User user) {
		List<PasswordResetToken> passwordResetTokens = passwordTokenRepository.findAllByUserId(user.getId());
		if(passwordResetTokens != null) {
			passwordTokenRepository.deleteAll(passwordResetTokens);
		}
		
		List<UserRegisterToken> userRegisterTokens = userRegisterTokenRepository.findAllByEmail(user.getEmail());
		if(userRegisterTokens != null) {
			userRegisterTokenRepository.deleteAll(userRegisterTokens);
		}
		
		repository.delete(user);
	}

}
