package com.trainingquizzes.english.model;

import com.trainingquizzes.english.enums.LevelType;

public class Average {
	
	private Long userId;
	private String name;
	private Subject subject;
	private User user;
	private LevelType level;
	private double average;
	
	private String bootstrapColor;
	
	public Average(String name, Subject subject, LevelType level, double average) {
		this.name = name;
		this.subject = subject;
		this.user = subject.getUser();
		this.level = level;
		this.average = average;
	}
	
	public Average(Long userId, Subject subject, LevelType level, double average) {
		this.userId = userId;
		this.subject = subject;
		this.user = subject.getUser();
		this.level = level;
		this.average = average;
	}
	
	public String getName() {
		return name;
	}

	public Subject getSubject() {
		return subject;
	}

	public LevelType getLevel() {
		return level;
	}
	
	public String getLevelCapitalize() {
		return this.level.toString().substring(0, 1).toUpperCase() + this.level.toString().substring(1).toLowerCase();
	}

	public double getAverage() {
		return average;
	}

	public double getAverageForMeter() {
		return getAverage() * 10;
	}
	
	public void setBootstrapColor(String bootstrapColor) {
		this.bootstrapColor = bootstrapColor;
	}

	public String getBootstrapColor() {
		return bootstrapColor;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

}
