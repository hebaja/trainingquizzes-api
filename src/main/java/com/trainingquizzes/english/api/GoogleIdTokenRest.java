//package com.trainingquizzes.english.api;
//
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.trainingquizzes.english.dto.ApiUserDto;
//import com.trainingquizzes.english.enums.AccountType;
//import com.trainingquizzes.english.enums.Roles;
//import com.trainingquizzes.english.model.Account;
//import com.trainingquizzes.english.model.Authority;
//import com.trainingquizzes.english.model.User;
//import com.trainingquizzes.english.oauth.OAuth2UserInfo;
//import com.trainingquizzes.english.repository.UserRepository;
//
//@RestController
//@RequestMapping("/api/user/google")
//public class GoogleIdTokenRest {
//	
//	@Autowired
//	private UserRepository userRepository;
//	
//	@Value("${spring.security.oauth2.client.registration.google.client-id}")
//	private String googleClientId;
//	
//	@PostMapping("{idToken}")
//	public ApiUserDto googleUser(@PathVariable("idToken") String idTokenString) throws GeneralSecurityException, IOException {
//			
//		HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
//			
//		final JacksonFactory jacksonFactory = new JacksonFactory();
//		
//		OAuth2UserInfo userInfo = new OAuth2UserInfo();
//			
//		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
//				.setAudience(Collections.singletonList(googleClientId))
//				.build();
//			
//		GoogleIdToken idToken = verifier.verify(idTokenString);
//		System.out.println("Rest idToken ---> " + idToken);
//	
//			if(idToken != null) {
//				Payload payload = idToken.getPayload();
//				
//				userInfo.setFirst_name((String) payload.get("given_name"));
//				userInfo.setEmail(payload.getEmail());
//				userInfo.setId(payload.getSubject());
//				userInfo.setImageUrl((String) payload.get("picture"));
//				updateUser(userInfo);
//				
//				return new ApiUserDto(userInfo);
//			}
//			System.out.println("return null");
//			return null;
//	}
//
//	private void updateUser(OAuth2UserInfo userInfo) {
//		Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());
//		User user = userOptional.orElse(null);
//		if(user == null) {
//			System.out.println("user is null");
//			user = new User();
//			user.setEmail(userInfo.getEmail());
//			user.setUsername(userInfo.getFirst_name());
//			user.setUid(userInfo.getId());
//			user.setEnabled(true);
//			
//			Authority authority = new Authority(Roles.ROLE_USER);
//			List<Authority> roles = Arrays.asList(authority);
//			
//			user.setRoles(roles);
//			
//			List<Account> accounts = new ArrayList<>();
//			accounts.add(new Account(AccountType.GOOGLE));
//			user.setAccounts(accounts);
//			
//			userRepository.save(user);
//			
//		} else if (user.getAccounts().contains(new Account(AccountType.EMAIL)) && 
//				!user.getAccounts().contains(new Account(AccountType.GOOGLE)) && 
//				!user.getAccounts().contains(new Account(AccountType.FACEBOOK))){
//			
//			user.setUsername(userInfo.getFirst_name());
//			user.setUid(userInfo.getId());
//			
//			List<Account> accounts = new ArrayList<>();
//			accounts.add(new Account(AccountType.GOOGLE));
//			accounts.add(new Account(AccountType.EMAIL));
//
//			user.setAccounts(accounts);
//			
//			userRepository.save(user);
//		} else if (user.getAccounts().contains(new Account(AccountType.FACEBOOK)) &&
//				!user.getAccounts().contains(new Account(AccountType.EMAIL))) {
//			
//			user.setUsername(userInfo.getFirst_name());
//			user.setUid(userInfo.getId());
//			
//			List<Account> accounts = new ArrayList<>();
//			accounts.add(new Account(AccountType.GOOGLE));
//			user.setAccounts(accounts);
//			
//			userRepository.save(user);
//			
//		} else if (user.getAccounts().contains(new Account(AccountType.FACEBOOK)) &&
//				user.getAccounts().contains(new Account(AccountType.EMAIL))) {
//			
//			user.setUsername(userInfo.getFirst_name());
//			user.setUid(userInfo.getId());
//			
//			List<Account> accounts = new ArrayList<>();
//			accounts.add(new Account(AccountType.GOOGLE));
//			accounts.add(new Account(AccountType.EMAIL));
//			user.setAccounts(accounts);
//			
//			userRepository.save(user);
//			
//		}
//	}
//
//}
