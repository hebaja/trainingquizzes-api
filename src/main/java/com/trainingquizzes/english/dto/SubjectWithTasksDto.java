package com.trainingquizzes.english.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Subject;

public class SubjectWithTasksDto {
	
	private Long id;
	private String title;
	private LevelType level;
	private SubjectWithUserDto userDto;
	private List<TaskDto> tasksDto;
	
	public SubjectWithTasksDto(Subject subject) {
		this.id = subject.getId();
		this.title = subject.getTitle();
		this.level = subject.getLevel();
		this.userDto = new SubjectWithUserDto(subject.getUser());
		this.tasksDto = subject.getTasks().stream().map(TaskDto::new).collect(Collectors.toList());
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
	
	@JsonProperty("user")
	public SubjectWithUserDto getUserDto() {
		return userDto;
	}

	@JsonProperty("tasks")
	public List<TaskDto> getTasksDto() {
		return tasksDto;
	}
	
	public static List<SubjectWithTasksDto> convertList(List<Subject> subjects) {
		return subjects.stream().map(SubjectWithTasksDto::new).collect(Collectors.toList());
	}

	public static SubjectWithTasksDto convertFromSubject(Subject subject) {
		return new SubjectWithTasksDto(subject);
	}
	
}
