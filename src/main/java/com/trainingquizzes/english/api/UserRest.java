package com.trainingquizzes.english.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.trainingquizzes.english.dto.UserWithoutSubjectsDto;
import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.form.UserForm;
import com.trainingquizzes.english.model.Authority;
import com.trainingquizzes.english.model.Quest;
import com.trainingquizzes.english.model.TemporaryTrialDataStore;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.PasswordResetTokenRepository;
import com.trainingquizzes.english.repository.QuestRepository;
import com.trainingquizzes.english.repository.TemporaryTrialDataStoreRepository;
import com.trainingquizzes.english.repository.TrialRepository;
import com.trainingquizzes.english.repository.UserRegisterTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.token.PasswordResetToken;
import com.trainingquizzes.english.token.UserRegisterToken;

@RestController
@RequestMapping("/api/user")
public class UserRest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private QuestRepository questRepository;
	
	@Autowired
	private TrialRepository trialRepositoy;
	
	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;
	
	@Autowired
	private UserRegisterTokenRepository userRegisterTokenRepository;
	
	@Autowired
	private TemporaryTrialDataStoreRepository temporaryTrialDataStoreRepository;
	
	Logger logger = LoggerFactory.getLogger(UserRest.class);
	
	@Value("${spring-english-training-quizzes-email-admin}")
	private String emailAdmin;
	
	@GetMapping
	public ResponseEntity<UserDto> userById(@RequestParam("userId") Long userId) {
		if(userId != null) {
			Optional<User> optionalUser = userRepository.findById(userId);
			if(optionalUser.isPresent()) return ResponseEntity.ok(new UserDto(optionalUser.get()));
			else return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("by-email{email}")
	public ResponseEntity<UserDtoNoPassword> byEmail(@RequestParam("email") String email) {
		if(email != null) {
			User user = userRepository.findByEmail(email).orElse(null);
			if(user != null) return ResponseEntity.ok(new UserDtoNoPassword(user));
			else return ResponseEntity.noContent().build();
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("uid{uid}")
	public ResponseEntity<UserDto> user(@RequestParam("uid") String uid) {
		if(uid != null) {
			User user = userRepository.findByUid(uid).orElse(null);
			if(user != null) return ResponseEntity.ok(new UserDto(user));
			else return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@RequestMapping("email/{email}")
	public boolean emailExists(@PathVariable("email") String email) {
		return userRepository.existsByEmail(email);
	}
	
	//NEED TO CHECK IF THIS ENDPOINT IS BEING USED
	@PostMapping
	public ResponseEntity<UserDto> register(@Valid @RequestBody UserForm userForm, UriComponentsBuilder uriBuilder) {
		User user = userForm.convert();
		userRepository.save(user);
		URI uri = uriBuilder.path("/user/{uid}").buildAndExpand(user.getUid()).toUri();
		return ResponseEntity.created(uri).body(new UserDto(user));
	}
	
	@PostMapping("update-roles")
	public ResponseEntity<UserDto> updateRoles(@Valid @RequestBody UserForm userForm) {
		if(userForm != null) {
			Optional<User> userOptional = userRepository.findById(userForm.getId()); 
			if(userOptional.isPresent() && !userForm.getRoles().isEmpty()) {
				Set<Authority> roles = userForm.getRoles().stream().map(Authority::new).collect(Collectors.toSet());
				userOptional.get().setRoles(roles);
				return ResponseEntity.ok(new UserDto(userRepository.save(userOptional.get())));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("teachers")
	public ResponseEntity<Page<UserWithoutSubjectsDto>> all(@RequestParam(required = false) String query, Pageable pagination) {
		if(query == null) {
			Page<User> users = userRepository.findAllTeachers(pagination);
			return ResponseEntity.ok(UserWithoutSubjectsDto.convertToPageable(users));
		} else {
			String searchQuery = "%" + query + "%";
			Page<User> users = userRepository.findAllTeachersByUsernameAndEmailLikeIgnoreCase(searchQuery, pagination);
			return ResponseEntity.ok(UserWithoutSubjectsDto.convertToPageable(users));
		}
	}
	
	@DeleteMapping
	public ResponseEntity<Long> delete(@RequestParam Long userId) {
		
		Optional<User> userOptional = userRepository.findById(userId);
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			List<Quest> subscribedQuests = questRepository.findAllById(user.getSubscribedQuestsIds());
			Optional<List<TemporaryTrialDataStore>> temporaryTrialDataStoreListOptional = temporaryTrialDataStoreRepository.findAllByUser(user);
			boolean isTeacher = user.getRoles().stream().anyMatch(authority -> authority.getRole().equals(Roles.ROLE_TEACHER));
			
			if(!subscribedQuests.isEmpty()) {
				Set<Long> ids = subscribedQuests.stream().map(Quest::getId).collect(Collectors.toSet());
				List<Quest> questsFound = questRepository.findAllById(ids); 
				questsFound.forEach(quest -> quest.getSubscribedUsersIds().remove(user.getId()));
				questRepository.saveAll(questsFound);
			}
			
			if(isTeacher) {
				Optional<List<Quest>> questsOptional = questRepository.findAllByUser(user);
				if(!questsOptional.isEmpty()) {
					List<Quest> userQuests = questsOptional.get();
										
					userQuests.forEach(quest -> {
						quest.getTrials().forEach(trial -> {
							temporaryTrialDataStoreRepository.deleteByTrial(trial);
							trialRepositoy.delete(trial);
						});
					});
				}
			}
			
			if(!temporaryTrialDataStoreListOptional.isEmpty()) {
				List<TemporaryTrialDataStore> temporaryTrialDataStorelist = temporaryTrialDataStoreListOptional.get();
				temporaryTrialDataStoreRepository.deleteAll(temporaryTrialDataStorelist);
			}
			
			try {
				removeUser(user);
				
				return ResponseEntity.ok(user.getId());
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
		}
		
		return ResponseEntity.badRequest().build();
	}

	private void removeUser(User user) {
		trialRepositoy.deleteBySubscribedUser(user);
		List<PasswordResetToken> passwordResetTokens = passwordTokenRepository.findAllByUserId(user.getId());
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
