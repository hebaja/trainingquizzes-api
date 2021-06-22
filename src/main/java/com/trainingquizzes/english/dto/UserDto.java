package com.trainingquizzes.english.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.trainingquizzes.english.model.User;

public class UserDto {

	private Long id;
	private String uid;
	private String username;
	private String email;
	private String password;
	
	public UserDto(User user) {
		this.id = user.getId();
		this.uid = user.getUid();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.password = user.getPassword();
	}
	
	public Long getId() {
		return id;
	}
	public String getUid() {
		return uid;
	}
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	

	public static List<UserDto> convertList(List<User> users) {
		return users.stream().map(UserDto::new).collect(Collectors.toList());
	}

	public static UserDto convert(User user) {	
		return null;
	}
}
