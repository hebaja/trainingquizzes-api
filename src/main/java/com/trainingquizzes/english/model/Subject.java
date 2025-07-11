package com.trainingquizzes.english.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.trainingquizzes.english.enums.LevelType;

@Entity
public class Subject {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String title;
	
	@Column(name = "public")
	private boolean publicSubject;
    
	@ManyToOne
	private User user;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Quest> quest;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Task> tasks;
	
	@OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Exercise> exercises;
	
	@Enumerated(EnumType.STRING)
    private LevelType level;
	
	private LocalDateTime creationDate = LocalDateTime.now();
	
	public Subject() {}
	
	public Subject(String title, List<Task> tasks, User user, LevelType level, boolean isPublic) {
		this.title = title;
		this.tasks = tasks;
		this.user = user;
		this.level = level;
		this.setPublicSubject(isPublic);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public LevelType getLevel() {
		return level;
	}

	public void setLevel(LevelType level) {
		this.level = level;
	}
	
	public List<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(List<Exercise> exercises) {
		this.exercises = exercises;
	}

	@Transient
	public String getLevelCapitalize() {
		return this.level.toString().substring(0, 1).toUpperCase() + this.level.toString().substring(1).toLowerCase();
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public boolean isPublicSubject() {
		return publicSubject;
	}

	public void setPublicSubject(boolean publicSubject) {
		this.publicSubject = publicSubject;
	}
	
}
