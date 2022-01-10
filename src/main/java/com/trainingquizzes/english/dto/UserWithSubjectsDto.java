package com.trainingquizzes.english.dto;

import java.util.List;

import com.trainingquizzes.english.model.User;

public class UserWithSubjectsDto {
	
	private String uid;
	private String email;
	private String username;
	private List<SubjectDto> subjects;
	
	public UserWithSubjectsDto(User user) {
		this.uid = user.getUid();
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.subjects = SubjectDto.convertList(user.getSubjects());
	}

	public String getUid() {
		return uid;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public List<SubjectDto> getSubjects() {
		return subjects;
	}
	
	public static UserWithSubjectsDto convert(User user) {
		return new UserWithSubjectsDto(user);
	}

}
