package com.trainingquizzes.english.dto;

import com.trainingquizzes.english.model.User;

public class ReducedUserDto {

	private Long id;
	private String uid;
	private String username;
	private String email;

	public ReducedUserDto(User user) {
		this.id = user.getId();
		this.uid = user.getUid();
		this.username = user.getUsername();
		this.email = user.getEmail();
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

}
