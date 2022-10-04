package com.trainingquizzes.english.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trainingquizzes.english.model.Subject;

public class SubjectDto {
	
	private Long id;
	private String title;
	private String level;
	private String creationDate;
	private UserWithoutSubjectsDto userDto;
	
	public SubjectDto(Subject subject) {
		this.id = subject.getId();
		this.title = subject.getTitle();
		this.level = subject.getLevelCapitalize();
		this.creationDate = subject.getCreationDate().toString();
		this.userDto = new UserWithoutSubjectsDto(subject.getUser());
	}

	public static Page<SubjectDto> convertToPageable(Page<Subject> subjects) {
		return subjects.map(SubjectDto::new);
	}
	
	public static List<SubjectDto> convertList(List<Subject> subjects) {
		return subjects.stream().map(SubjectDto::new).collect(Collectors.toList());
	}
	
	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getLevel() {
		return level;
	}

	public static SubjectDto convertFromSubject(Subject subject) {
		return new SubjectDto(subject);
	}

	@JsonProperty("user")
	public UserWithoutSubjectsDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserWithoutSubjectsDto userDto) {
		this.userDto = userDto;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
}
