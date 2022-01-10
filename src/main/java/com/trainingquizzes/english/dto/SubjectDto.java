package com.trainingquizzes.english.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Subject;

public class SubjectDto {
	
	private Long id;
	private String title;
	private LevelType level;
	
	public SubjectDto(Subject subject) {
		this.id = subject.getId();
		this.title = subject.getTitle();
		this.level = subject.getLevel();
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public LevelType getLevel() {
		return level;
	}

	public static List<SubjectDto> convertList(List<Subject> subjects) {
		return subjects.stream().map(SubjectDto::new).collect(Collectors.toList());
	}
	
	public static SubjectDto convertFromSubject(Subject subject) {
		return new SubjectDto(subject);
	}
}
