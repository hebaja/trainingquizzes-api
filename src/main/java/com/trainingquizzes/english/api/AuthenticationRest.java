package com.trainingquizzes.english.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.gson.Gson;
import com.trainingquizzes.english.config.TokenService;
import com.trainingquizzes.english.dto.TokenDto;
import com.trainingquizzes.english.dto.UserDtoNoPassword;
import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.form.LoginForm;
import com.trainingquizzes.english.model.Account;
import com.trainingquizzes.english.model.Authority;
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

	@PostMapping
	public ResponseEntity<TokenDto> auth(@RequestBody @Valid LoginForm form) {
		UsernamePasswordAuthenticationToken loginData = form.convert();
		try {
			Authentication authentication = authManager.authenticate(loginData);
			String token = tokenService.generateToken(authentication);
			User user = userRepository.findById(tokenService.getUserId(token)).orElse(null);
			return ResponseEntity.ok(new TokenDto(token, "Bearer", user));	
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
	
	@PostMapping("facebook")
	public ResponseEntity<UserDtoNoPassword> facebookAuth(@RequestBody String accessToken) {
		
        RestTemplate restTemplate = new RestTemplate();
        String facebook = null;
        final String fields = "id,email,first_name,last_name";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://graph.facebook.com/me")
                    .queryParam("access_token", accessToken).queryParam("fields", fields);

            facebook = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
            
            String facebookUnescaped = StringEscapeUtils.unescapeJava(facebook.toString());
            
            Gson gson = new Gson();
            OAuth2UserInfo userInfo = gson.fromJson(facebookUnescaped, OAuth2UserInfo.class);
            
            User user = updateUser(userInfo, AccountType.FACEBOOK);
            
            return ResponseEntity.ok(new UserDtoNoPassword(user));
            
        } catch (HttpClientErrorException e) {
            System.out.println("Not able to authenticate from Facebook");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } catch (Exception exp) {
            System.out.println("User is not authorized to login into system" + exp);
            exp.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
		
	}
	
	@PostMapping("google")
	public ResponseEntity<UserDtoNoPassword> googleAuth(@RequestBody String receivedIdToken) {

		if(receivedIdToken != null) {
			GoogleIdTokenVerifier verifier = GoogleAuthenticationUtil.retrieveIdTokenVerifier();
			
			try {
				GoogleIdToken idToken = verifier.verify(receivedIdToken);
				if(idToken != null) {
					Payload payload = idToken.getPayload();
					OAuth2UserInfo userInfo = new OAuth2UserInfo();
					userInfo.setFirst_name((String) payload.get("given_name"));
					userInfo.setEmail(payload.getEmail());
					userInfo.setId(payload.getSubject());
					userInfo.setImageUrl((String) payload.get("picture"));
					User user = updateUser(userInfo, AccountType.GOOGLE);
					
					return ResponseEntity.ok(new UserDtoNoPassword(user));
				}
			} catch (GeneralSecurityException | IOException e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().build();
			}
		}
		return ResponseEntity.badRequest().build();
	}
	
	private User updateUser(OAuth2UserInfo userInfo, AccountType receivedAccount) {
		Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());
		User user = userOptional.orElse(null);
		
		if(user == null) {
			
			System.out.println("1");
			
			user = new User();
			user.setEmail(userInfo.getEmail());
			user.setUsername(userInfo.getFirst_name());
			user.setUid(userInfo.getId());
			user.setEnabled(true);
			
			Authority authority = new Authority(Roles.ROLE_USER);
			List<Authority> roles = Arrays.asList(authority);

			user.setRoles(roles);
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(receivedAccount));
			user.setAccounts(accounts);
			
			User savedUser = userRepository.save(user);
			return savedUser;
			
		} else if (user.getAccounts().contains(new Account(AccountType.EMAIL)) && 
				!user.getAccounts().contains(new Account(AccountType.GOOGLE)) && 
				!user.getAccounts().contains(new Account(AccountType.FACEBOOK))){
			
			System.out.println("2");
			
			user.setUsername(userInfo.getFirst_name());
			user.setUid(userInfo.getId());
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(receivedAccount));
			accounts.add(new Account(AccountType.EMAIL));

			user.setAccounts(accounts);
			
			User updatedUser = userRepository.save(user);
			return updatedUser;
		} else if (user.getAccounts().contains(new Account(AccountType.FACEBOOK)) &&
				!user.getAccounts().contains(new Account(AccountType.EMAIL))) {
			
			System.out.println("3");
			
			user.setUsername(userInfo.getFirst_name());
			user.setUid(userInfo.getId());
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(receivedAccount));
			user.setAccounts(accounts);
			
			User updatedUser = userRepository.save(user);
			return updatedUser;
		} else if (user.getAccounts().contains(new Account(AccountType.FACEBOOK)) &&
			user.getAccounts().contains(new Account(AccountType.EMAIL))) {
			
			System.out.println("4");
			
			user.setUsername(userInfo.getFirst_name());
			user.setUid(userInfo.getId());
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(AccountType.GOOGLE));
			accounts.add(new Account(AccountType.EMAIL));
			user.setAccounts(accounts);
			
			User updatedUser = userRepository.save(user);
			return updatedUser;
		} else if(user.getAccounts().contains(new Account(receivedAccount))) {
			user.setUsername(userInfo.getFirst_name());
			user.setUid(userInfo.getId());
			
			System.out.println("5");
			
//			List<Account> accounts = new ArrayList<>();
//			accounts.add(new Account(AccountType.GOOGLE));
//			user.setAccounts(accounts);
			
			User updatedUser = userRepository.save(user);
			return updatedUser;
		}
		return null;
	}
	
}
