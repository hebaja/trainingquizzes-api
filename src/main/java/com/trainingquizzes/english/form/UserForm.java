package com.trainingquizzes.english.form;

import com.trainingquizzes.english.model.User;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UserForm {
	
	private String username;
	
	private String email;
	
	private String password;
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public User convert() {
		User user = new User();
		
		String passwordHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
		
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordHashString);
		
		return user;
	}
}
