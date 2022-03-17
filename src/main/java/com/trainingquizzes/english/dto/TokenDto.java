package com.trainingquizzes.english.dto;

import com.trainingquizzes.english.model.User;

public class TokenDto {

	private String token;
	private String type;
	private UserDtoNoPassword user;

	public TokenDto(String token, String type, User user) {
		this.token = token;
		this.type = type;
		this.user = new UserDtoNoPassword(user);
	}

	public String getToken() {
		return token;
	}

	public String getType() {
		return type;
	}

	public UserDtoNoPassword getUser() {
		return user;
	}
	
}
