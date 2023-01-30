package com.trainingquizzes.english.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.trainingquizzes.english.model.User;

public class UserWithoutSubjectsDto {

	private Long id;
	private String uid;
	private String username;
	private String email;
	private String pictureUrl;
	private List<String> roles = new ArrayList<>();

	public UserWithoutSubjectsDto(User user) {
		this.id = user.getId();
		this.uid = user.getUid();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.pictureUrl = user.getPictureUrl();
		List<RolesDto> list = RolesDto.convertToList(user.getAuthorities());
		list.forEach(role -> this.roles.add(role.getRole()));
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

	public static List<UserWithoutSubjectsDto> convertList(List<User> users) {
		return users.stream().map(UserWithoutSubjectsDto::new).collect(Collectors.toList());
	}

	public static Page<UserWithoutSubjectsDto> convertToPageable(Page<User> users) {
		return users.map(UserWithoutSubjectsDto::new);
	}

	public static UserWithoutSubjectsDto convert(User user) {
		return null;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
