package com.trainingquizzes.english.api;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api/user")
public class UserRest {

	@Autowired
	private UserRepository repository;
	
	@GetMapping("{uid}")
	public UserDto user(@PathVariable("uid") String uid) {
		Optional<User> userOptional = repository.findByUid(uid);
		User user = userOptional.get();
		return new UserDto(user);
	}
	
	@RequestMapping("email/{email}")
	public boolean emailExists(@PathVariable("email") String email) {
		boolean existsByEmail = repository.existsByEmail(email);
		return existsByEmail;
	}
	
	@PostMapping
	public ResponseEntity<UserDto> register(@Valid @RequestBody UserForm userForm, UriComponentsBuilder uriBuilder) {
		User user = userForm.convert();
		repository.save(user);
		URI uri = uriBuilder.path("/user/{uid}").buildAndExpand(user.getUid()).toUri();
		return ResponseEntity.created(uri).body(new UserDto(user));
	}
}
