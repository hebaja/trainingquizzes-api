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
//	private Collection<? extends GrantedAuthority> roles;
	private List<String> roles = new ArrayList<String>();
	private String pictureUrl;

	public UserDtoNoPassword(User user) {
		this.id = user.getId();
		this.uid = user.getUid();
		this.username = user.getUsername();
		this.email = user.getEmail();
		List<RolesDto> list = RolesDto.convertToList(user.getAuthorities());
		list.forEach(role -> this.roles.add(role.getRole()));
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

	public static UserDtoNoPassword convert(User user) {
		return null;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
}
