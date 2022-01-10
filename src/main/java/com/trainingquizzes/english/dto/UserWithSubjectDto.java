package com.trainingquizzes.english.dto;

import java.util.ArrayList;
import java.util.List;

import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.User;

public class UserWithSubjectDto {
	
	private String uid;
	private String email;
	private String username;
	private List<SubjectDto> subjects = new ArrayList<SubjectDto>();
	
	public UserWithSubjectDto(User user, Subject subject) {
		this.uid = user.getUid();
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.subjects.add(SubjectDto.convertFromSubject(subject));
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
	
	public static UserWithSubjectDto convert(User user, Subject subject) {
		return new UserWithSubjectDto(user, subject);
	}

}
