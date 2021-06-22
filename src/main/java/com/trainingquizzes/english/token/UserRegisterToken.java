package com.trainingquizzes.english.token;

import javax.persistence.Entity;

@Entity
public class UserRegisterToken extends Token {
	 
	private String username;

	@Deprecated
	public UserRegisterToken() {}

	public UserRegisterToken(String token, String username, String email, String password) {
		this.token = token;
		this.username = username;
		this.email = email;
		this.pasword = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
