//package com.trainingquizzes.english.oauth;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import com.trainingquizzes.english.enums.AccountType;
//import com.trainingquizzes.english.enums.Roles;
//import com.trainingquizzes.english.model.Account;
//import com.trainingquizzes.english.model.Authority;
//import com.trainingquizzes.english.model.User;
//import com.trainingquizzes.english.repository.UserRepository;
//
//@Service
//public class FacebookOAuth2UserService extends DefaultOAuth2UserService{
//	
//	@Autowired
//	private UserRepository userRepository;
//
//	@Override
//	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		OAuth2User oAuth2User = super.loadUser(userRequest);
//		return processOAuth2User(userRequest, oAuth2User);
//	}
//
//	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
//		Map<String, Object> attributes = oAuth2User.getAttributes();
//		FacebookOAuth2UserInfo userInfo = new FacebookOAuth2UserInfo();
//		
//		userInfo.setId((String) attributes.get("id"));
//		userInfo.setName((String) attributes.get("name"));
//		userInfo.setEmail((String) attributes.get("email"));
//		
//		Optional<User> userOptional = userRepository.findByEmail((String) attributes.get("email"));
//		
//		User user = userOptional.orElse(null);
//		
//		if(user == null) {
//			user = new User();
//			user.setUid(userInfo.getId());
//			user.setEmail(userInfo.getEmail());
//			user.setUsername(userInfo.getName());
//			user.setEnabled(true);
//			
//			List<Account> accounts = new ArrayList<>();
//			accounts.add(new Account(AccountType.FACEBOOK));
//			user.setAccounts(accounts);
//			
//			Authority authority = new Authority(Roles.ROLE_USER);
//			List<Authority> roles = Arrays.asList(authority);
//			
//			user.setRoles(roles);
//			
//			userRepository.save(user);
//			
//		} else if (user.getAccounts().contains(new Account(AccountType.EMAIL)) && 
//				!user.getAccounts().contains(new Account(AccountType.GOOGLE)) && 
//				!user.getAccounts().contains(new Account(AccountType.FACEBOOK))) {
//			
//			user.setUsername(userInfo.getName());
//			user.setUid(userInfo.getId());
//			
//			List<Account> accounts = new ArrayList<>();
//			accounts.add(new Account(AccountType.FACEBOOK));
//			accounts.add(new Account(AccountType.EMAIL));
//			user.setAccounts(accounts);
//			
//			userRepository.save(user);
//		} else if (user.getAccounts().contains(new Account(AccountType.GOOGLE)) &&
//				!user.getAccounts().contains(new Account(AccountType.EMAIL))) {
//			
//			user.setUsername(userInfo.getName());
//			user.setUid(userInfo.getId());
//			
//			List<Account> accounts = new ArrayList<>();
//			accounts.add(new Account(AccountType.FACEBOOK));
//			user.setAccounts(accounts);
//			
//			userRepository.save(user);
//		} else if (user.getAccounts().contains(new Account(AccountType.GOOGLE)) &&
//				user.getAccounts().contains(new Account(AccountType.EMAIL))) {
//			
//			user.setUsername(userInfo.getName());
//			user.setUid(userInfo.getId());
//			
//			List<Account> accounts = new ArrayList<>();
//			accounts.add(new Account(AccountType.FACEBOOK));
//			accounts.add(new Account(AccountType.EMAIL));
//			user.setAccounts(accounts);
//			
//			userRepository.save(user);
//		}
//		return oAuth2User;
//	}
//}
