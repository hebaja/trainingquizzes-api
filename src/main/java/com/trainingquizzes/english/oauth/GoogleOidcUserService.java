package com.trainingquizzes.english.oauth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.model.Account;
import com.trainingquizzes.english.model.Authority;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.UserRepository;

@Service
public class GoogleOidcUserService extends OidcUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	private HttpTransport transport;
	
	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser oidcUser = super.loadUser(userRequest);
		
		OAuth2UserInfo userInfo = new OAuth2UserInfo();
		
		OidcIdToken oidcToken = oidcUser.getIdToken();
		
		String tokenValue = oidcToken.getTokenValue();
		
		try {
			transport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		
		final JacksonFactory jacksonFactory = new JacksonFactory();
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
				.setAudience(Collections.singletonList("984346929011-3vfjq0ra18qbuh16ejrt40mdf8807bgb.apps.googleusercontent.com"))
				.build();
		
		try {
			GoogleIdToken idToken = verifier.verify(tokenValue);
			if(idToken != null) {
				Payload payload = idToken.getPayload();
				userInfo.setName((String) payload.get("given_name"));
				userInfo.setEmail(payload.getEmail());
				userInfo.setId(payload.getSubject());
				userInfo.setImageUrl((String) payload.get("picture"));
				updateUser(userInfo);
			}
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		return oidcUser;
	}

	private void updateUser(OAuth2UserInfo userInfo) {
		Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());
		User user = userOptional.orElse(null);
		
		if(user == null) {
			user = new User();
			user.setEmail(userInfo.getEmail());
			user.setUsername(userInfo.getName());
			user.setUid(userInfo.getId());
			user.setEnabled(true);
			
			Authority authority = new Authority(Roles.ROLE_USER);
			List<Authority> roles = Arrays.asList(authority);

			user.setRoles(roles);
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(AccountType.GOOGLE));
			user.setAccounts(accounts);
			
			userRepository.save(user);
			
		} else if (user.getAccounts().contains(new Account(AccountType.EMAIL)) && 
				!user.getAccounts().contains(new Account(AccountType.GOOGLE)) && 
				!user.getAccounts().contains(new Account(AccountType.FACEBOOK))){
			
			user.setUsername(userInfo.getName());
			user.setUid(userInfo.getId());
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(AccountType.GOOGLE));
			accounts.add(new Account(AccountType.EMAIL));

			user.setAccounts(accounts);
			
			userRepository.save(user);
		} else if (user.getAccounts().contains(new Account(AccountType.FACEBOOK)) &&
				!user.getAccounts().contains(new Account(AccountType.EMAIL))) {
			
			user.setUsername(userInfo.getName());
			user.setUid(userInfo.getId());
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(AccountType.GOOGLE));
			user.setAccounts(accounts);
			
			userRepository.save(user);
			
		} else if (user.getAccounts().contains(new Account(AccountType.FACEBOOK)) &&
				user.getAccounts().contains(new Account(AccountType.EMAIL))) {
			
			user.setUsername(userInfo.getName());
			user.setUid(userInfo.getId());
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(AccountType.GOOGLE));
			accounts.add(new Account(AccountType.EMAIL));
			user.setAccounts(accounts);
			
			userRepository.save(user);
		}
	}
}
