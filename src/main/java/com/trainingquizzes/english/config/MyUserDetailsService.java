package com.trainingquizzes.english.config;

import com.trainingquizzes.english.model.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trainingquizzes.english.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	public UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> userOptional = repository.findByEmail(email);
		
		User user = userOptional.get();
		
		if(user == null) {
			throw new UsernameNotFoundException("Not found");
		}
		MyUserPrincipal userPrincipal = new MyUserPrincipal(user);
		return userPrincipal;
	}
	
	

}
