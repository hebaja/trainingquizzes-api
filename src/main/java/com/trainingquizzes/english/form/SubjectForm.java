package com.trainingquizzes.english.form;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trainingquizzes.english.enums.LevelType;

public class SubjectForm {

	private Long id;
	private String title;
	private LevelType level;
	private List<TaskForm> tasks;
	private UserForm user;
	private boolean publicSubject;
	private boolean userOwner;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LevelType getLevel() {
		return level;
	}

	public void setLevel(LevelType level) {
		this.level = level;
	}

	public List<TaskForm> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskForm> tasks) {
		this.tasks = tasks;
	}

	public UserForm getUser() {
		return user;
	}

	public void setUser(UserForm user) {
		this.user = user;
	}

	public boolean isUserOwner() {
		return userOwner;
	}

	public void setUserOwner(boolean userOwner) {
		this.userOwner = userOwner;
	}

	public boolean isPublicSubject() {
		return publicSubject;
	}

	public void setPublicSubject(boolean publicSubject) {
		this.publicSubject = publicSubject;
	}

}
