package com.trainingquizzes.english.dto;

import com.trainingquizzes.english.oauth.OAuth2UserInfo;

public class ApiUserDto {
	
	private String username;
	private String email;
	
	public ApiUserDto(OAuth2UserInfo userInfo) {
		this.username = userInfo.getName();
		this.email = userInfo.getEmail();
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}
}
