package com.trainingquizzes.english.token;

import javax.persistence.Entity;

import com.trainingquizzes.english.enums.Roles;

@Entity
public class UserRegisterToken extends Token {
	 
	private String username;

	@Deprecated
	public UserRegisterToken() {}

	public UserRegisterToken(String token, String username, String email, String password, Roles role) {
		this.token = token;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
