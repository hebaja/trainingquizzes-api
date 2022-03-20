package com.trainingquizzes.english.api;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
@CrossOrigin
@RequestMapping("/api/user/email")
public class EmailSignInRest {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("{email}")
	public ResponseEntity<UserDto> emailUser(@RequestBody UserForm userForm, UriComponentsBuilder uriBuilder) {
		User userSent = userForm.convert();
		Optional<User> userOptional = userRepository.findByEmail(userSent.getEmail());
		User userFound = userOptional.orElse(null);
		
		if(userFound != null) {
			URI uri = uriBuilder.path("/user/email/{id}").buildAndExpand(userSent.getId()).toUri();
			return ResponseEntity.ok(new UserDto(userFound));
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<UserDto> emailUserAlternate(@RequestBody UserForm userForm, UriComponentsBuilder uriBuilder) {
		User userSent = userForm.convert();
		Optional<User> userOptional = userRepository.findByEmail(userSent.getEmail());
		User userFound = userOptional.orElse(null);
		
		if(userFound != null) {
			URI uri = uriBuilder.path("/user/email/{id}").buildAndExpand(userSent.getId()).toUri();
			return ResponseEntity.ok(new UserDto(userFound));
		}
		return ResponseEntity.notFound().build();
	}
}
