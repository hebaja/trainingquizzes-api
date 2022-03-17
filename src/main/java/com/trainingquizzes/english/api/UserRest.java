package com.trainingquizzes.english.api;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.trainingquizzes.english.dto.UserDto;
import com.trainingquizzes.english.dto.UserDtoNoPassword;
import com.trainingquizzes.english.dto.UserWithSubjectsDto;
import com.trainingquizzes.english.form.UserForm;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserRest {

	@Autowired
	private UserRepository repository;
	
	@Value("${spring-english-training-quizzes-email-admin}")
	private String emailAdmin;
	
	@GetMapping("by-email{email}")
	public ResponseEntity<UserDtoNoPassword> byEmail(@RequestParam("email") String email) {
		User user = repository.findByEmail(email).orElse(null);
		if(user != null) {
			return ResponseEntity.ok(new UserDtoNoPassword(user));
		}
		return ResponseEntity.badRequest().build();
				
	}
	
	@GetMapping("uid{uid}")
	public ResponseEntity<UserDto> user(@RequestParam("uid") String uid) {
		
		User user = repository.findByUid(uid).orElse(null);;
		if(user != null) {
			return ResponseEntity.ok(new UserDto(user));
		}
		return ResponseEntity.notFound().build();
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
	
	@GetMapping("subjects")
	@Cacheable(value = "userWithSubjectsList")
	public UserWithSubjectsDto findSubjects() {
		Optional<User> userOptional = repository.findByEmail(emailAdmin);
		return UserWithSubjectsDto.convert(userOptional.orElse(null));
	}
}
