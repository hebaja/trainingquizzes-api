package com.trainingquizzes.english.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trainingquizzes.english.model.Subject;

public class SubjectWithoutUserDto {
	
	private Long id;
	private String title;
	private String level;
	private boolean publicSubject;
	private String creationDate;
	
	public SubjectWithoutUserDto(Subject subject) {
		this.id = subject.getId();
		this.title = subject.getTitle();
		this.level = subject.getLevelCapitalize();
		this.publicSubject = subject.isPublicSubject();
		this.creationDate = subject.getCreationDate().toString();
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

	public static Page<SubjectWithoutUserDto> convertToPageable(Page<Subject> subjects) {
		return subjects.map(SubjectWithoutUserDto::new);
	}
	
	public static List<SubjectWithoutUserDto> convertList(List<Subject> subjects) {
		return subjects.stream().map(SubjectWithoutUserDto::new).collect(Collectors.toList());
	}
	
	public static SubjectWithoutUserDto convertFromSubject(Subject subject) {
		return new SubjectWithoutUserDto(subject);
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	@JsonProperty("public")
	public boolean isPublicSubject() {
		return publicSubject;
	}
}
