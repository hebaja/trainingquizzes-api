package com.trainingquizzes.english.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.trainingquizzes.english.model.User;

public class UserDto {

	private Long id;
	private String uid;
	private String username;
	private String email;
	private String pictureUrl;
	private List<SubjectDto> subjects = new ArrayList<>();
	private List<String> roles = new ArrayList<>();
	
	public UserDto(User user) {
		this.id = user.getId();
		this.uid = user.getUid();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.pictureUrl = user.getPictureUrl();
		this.subjects = user.getSubjects().stream().map(SubjectDto::new).collect(Collectors.toList());
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

	public static List<UserDto> convertList(List<User> users) {
		return users.stream().map(UserDto::new).collect(Collectors.toList());
	}
	
	public static Page<UserDto> convertToPageable(Page<User> users) {
		return users.map(UserDto::new);
	}

	public static UserDto convert(User user) {	
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

	public List<SubjectDto> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SubjectDto> subjects) {
		this.subjects = subjects;
	}

}
