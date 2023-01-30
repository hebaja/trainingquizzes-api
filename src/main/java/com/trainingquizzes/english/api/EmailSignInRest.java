package com.trainingquizzes.english.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.trainingquizzes.english.dto.UserDto;
import com.trainingquizzes.english.form.UserForm;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("/api/user/email")
public class EmailSignInRest {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("{email}")
	public ResponseEntity<UserDto> emailUser(@RequestBody UserForm userForm, UriComponentsBuilder uriBuilder) {
		User user = userRepository.findByEmail(userForm.convert().getEmail()).orElse(null);
		if(user != null) {
			return ResponseEntity.ok(new UserDto(user));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<UserDto> emailUserAlternate(@RequestBody UserForm userForm, UriComponentsBuilder uriBuilder) {
		User teacher = userRepository.findByEmail(userForm.convert().getEmail()).orElse(null);
		if(teacher != null) {
			return ResponseEntity.ok(new UserDto(teacher));
		}
		
		return ResponseEntity.notFound().build();
	}
}