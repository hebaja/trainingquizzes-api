package com.trainingquizzes.english.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.trainingquizzes.english.dto.ApiUserDto;
import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.model.Account;
import com.trainingquizzes.english.model.Authority;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.oauth.OAuth2UserInfo;
import com.trainingquizzes.english.repository.UserRepository;

@RestController
@RequestMapping("/api/user/facebook")
public class FacebookTokenRest {

	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("{idToken}")
	public ApiUserDto facebookUser(@PathVariable("idToken") String idTokenString) {
		
		System.out.println("here");
		
		OAuth2UserInfo userInfo = new OAuth2UserInfo();
		
		if(idTokenString != null) {
			FacebookClient facebookClient = new DefaultFacebookClient(idTokenString, Version.LATEST);
			System.out.println(idTokenString);
			com.restfb.types.User facebookUser = facebookClient.fetchObject("me", com.restfb.types.User.class,
					Parameter.with("fields", "id,email,first_name"));
			
			userInfo.setId(facebookUser.getId());
			userInfo.setEmail(facebookUser.getEmail());
			userInfo.setName(facebookUser.getFirstName());
			updateUser(userInfo);
			
			System.out.println(userInfo.getId());
			System.out.println(userInfo.getName());
			System.out.println(userInfo.getEmail());
			
			return new ApiUserDto(userInfo);
		}
		System.out.println("return null");
		return null;
	}

	private void updateUser(OAuth2UserInfo userInfo) {
		Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());;
		User user = userOptional.orElse(null);
		
		if(user == null) {
			user = new User();
			user.setUid(userInfo.getId());
			user.setEmail(userInfo.getEmail());
			user.setUsername(userInfo.getName());
			user.setEnabled(true);
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(AccountType.FACEBOOK));
			user.setAccounts(accounts);
			
			Authority authority = new Authority(Roles.ROLE_USER);
			List<Authority> roles = Arrays.asList(authority);
			user.setRoles(roles);
			
			userRepository.save(user);
			
		} else if (user.getAccounts().contains(new Account(AccountType.EMAIL)) && 
				!user.getAccounts().contains(new Account(AccountType.GOOGLE)) && 
				!user.getAccounts().contains(new Account(AccountType.FACEBOOK))) {
			
			user.setUsername(userInfo.getName());
			user.setUid(userInfo.getId());
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(AccountType.FACEBOOK));
			accounts.add(new Account(AccountType.EMAIL));
			user.setAccounts(accounts);
			
			userRepository.save(user);
		} else if (user.getAccounts().contains(new Account(AccountType.GOOGLE)) &&
				!user.getAccounts().contains(new Account(AccountType.EMAIL))) {
			
			user.setUsername(userInfo.getName());
			user.setUid(userInfo.getId());
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(AccountType.FACEBOOK));
			user.setAccounts(accounts);
			
			userRepository.save(user);
		} else if (user.getAccounts().contains(new Account(AccountType.GOOGLE)) &&
				user.getAccounts().contains(new Account(AccountType.EMAIL))) {
			
			user.setUsername(userInfo.getName());
			user.setUid(userInfo.getId());
			
			List<Account> accounts = new ArrayList<>();
			accounts.add(new Account(AccountType.FACEBOOK));
			accounts.add(new Account(AccountType.EMAIL));
			user.setAccounts(accounts);
			
			userRepository.save(user);
		}
		
	}
	
	
}
