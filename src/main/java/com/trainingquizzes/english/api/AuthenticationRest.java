package com.trainingquizzes.english.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.trainingquizzes.english.config.TokenService;
import com.trainingquizzes.english.dto.TokenDto;
import com.trainingquizzes.english.dto.UserDto;
import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.form.FacebookUserForm;
import com.trainingquizzes.english.form.GoogleUserForm;
import com.trainingquizzes.english.form.LoginForm;
import com.trainingquizzes.english.model.Account;
import com.trainingquizzes.english.model.Authority;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.oauth.OAuth2UserInfo;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.util.GoogleAuthenticationUtil;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthenticationRest {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TokenService tokenService;
	
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String googleClientId;
	
	@Value("${spring.security.oauth2.client.registration.facebook.client-id}")
	private String facebookClientId;
	
	@Value("${spring.mail.host}")
	private String mailHost;

	@PostMapping
	public ResponseEntity<TokenDto> auth(@RequestBody @Valid LoginForm form) {
		UsernamePasswordAuthenticationToken loginData = form.convert();
		try {
			Authentication authentication = authManager.authenticate(loginData);
			String token = tokenService.generateToken(authentication);
			Optional<User> userOptional = userRepository.findById(tokenService.getUserId(token));
			if(userOptional.isPresent()) {
				userOptional.get().getRoles().forEach(role -> System.out.println(role.getRole()));
				
				return ResponseEntity.ok(new TokenDto(token, "Bearer", userOptional.get()));	
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("google")
	public ResponseEntity<UserDto> googleAuth(@RequestBody GoogleUserForm form) {
		if(form != null) {
			GoogleIdTokenVerifier verifier =  GoogleAuthenticationUtil.retrieveIdTokenVerifier();
			try {
				GoogleIdToken idToken = verifier.verify(form.getIdToken());
				if(idToken != null) {
					Payload payload = idToken.getPayload();
					OAuth2UserInfo userInfo = new OAuth2UserInfo();
					userInfo.setFirst_name((String) payload.get("given_name"));
					userInfo.setEmail(payload.getEmail());
					userInfo.setId(payload.getSubject());
					userInfo.setPictureUrl((String) payload.get("picture"));
					userInfo.setRoles(form.getRoles());
					User user = resolveUser(userInfo, new Account(AccountType.GOOGLE));
					
					return ResponseEntity.ok(new UserDto(user));
				}
			} catch (GeneralSecurityException | IOException e) {
				e.printStackTrace();
				
				return ResponseEntity.badRequest().build();
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("facebook")
	public ResponseEntity<UserDto> facebookUser(@RequestBody FacebookUserForm form) {
		if(form != null) {
			OAuth2UserInfo userInfo = new OAuth2UserInfo();
			userInfo.setFirst_name(form.getUsername());
			userInfo.setEmail(form.getEmail());
			userInfo.setId(form.getUid());
			userInfo.setPictureUrl(form.getPictureUrl());
			userInfo.setRoles(form.getRoles());
			User savedUser = resolveUser(userInfo, new Account(AccountType.FACEBOOK));
			
			return ResponseEntity.ok(new UserDto(savedUser));
		}
		
		return ResponseEntity.badRequest().build();
	}

	private User resolveUser(OAuth2UserInfo userInfo, Account account) {
		User user = userRepository.findByEmail(userInfo.getEmail()).orElse(null);
		if(user == null) {
			User createdUser = createNewUser(userInfo, account);
			
			return userRepository.save(createdUser);
		} else {
			user.setUsername(userInfo.getFirst_name());
			user.setUid(userInfo.getId());
			user.setPictureUrl(userInfo.getPictureUrl());
			user.setSubjects(new ArrayList<Subject>());
			userInfo.getRoles().forEach(role -> user.getRoles().add(new Authority(role)));
			if(!user.getAccounts().contains(account)) {
				user.getAccounts().add(account);
			}
		}
		
		return userRepository.save(user); 
	}
	
	private User createNewUser(OAuth2UserInfo userInfo, Account account) {
		User user = new User();
		List<Account> accounts = new ArrayList<>();
		List<Authority> roles = new ArrayList<>();
		user.setUid(userInfo.getId());
		user.setUsername(userInfo.getFirst_name());
		user.setEmail(userInfo.getEmail());
		user.setPictureUrl(userInfo.getPictureUrl());
		user.setEnabled(true);
		user.setAccounts(accounts);
		user.getAccounts().add(account);
		user.setRoles(roles);
		user.setSubjects(new ArrayList<>());
		userInfo.getRoles().forEach(role -> user.getRoles().add(new Authority(role)));
		
		return user;
	}
	
}
