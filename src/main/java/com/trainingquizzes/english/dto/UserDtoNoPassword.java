package com.trainingquizzes.english.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.trainingquizzes.english.model.User;

public class UserDtoNoPassword {

	private Long id;
	private String uid;
	private String username;
	private String email;
	private List<RolesDto> roles = new ArrayList<>();
	private String pictureUrl;

	public UserDtoNoPassword(User user) {
		this.id = user.getId();
		this.uid = user.getUid();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.roles = RolesDto.convertToList(user.getAuthorities()).stream().collect(Collectors.toList());
		this.pictureUrl = user.getPictureUrl();
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

	public static List<UserDtoNoPassword> convertList(List<User> users) {
		return users.stream().map(UserDtoNoPassword::new).collect(Collectors.toList());
	}

	public List<RolesDto> getRoles() {
		return roles;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
}
