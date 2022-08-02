package com.trainingquizzes.english.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.trainingquizzes.english.enums.LevelType;

@Entity
public class Subject {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String title;
    
	@ManyToOne
	private User user;
	
	@OneToMany
//	@Cascade(CascadeType.ALL)
	private Set<Quest> quest;
	
	@OneToMany(fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
	private List<Task> tasks;
	
	@OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.REMOVE)
//	@Cascade(CascadeType.ALL)
	private List<Exercise> exercises;
	
	@Enumerated(EnumType.STRING)
    private LevelType level;
	
	private LocalDateTime creationDate = LocalDateTime.now();
	
	public Subject() {}
	
	public Subject(String title, List<Task> tasks, User user, LevelType level) {
		this.title = title;
		this.tasks = tasks;
		this.user = user;
		this.level = level;
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
	
}
