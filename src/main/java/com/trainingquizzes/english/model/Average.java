package com.trainingquizzes.english.model;

import com.trainingquizzes.english.enums.LevelType;

public class Average {
	
	private Long userId;
	private String name;
	private Subject subject;
	private LevelType level;
	private double average;
	private double averageForMeter;
	
	private String bootstrapColor;
	
	public Average(String name, Subject subject, LevelType level, double average) {
		this.name = name;
		this.subject = subject;
		this.level = level;
		this.average = average;
	}
	
	public Average(Long userId, Subject subject, LevelType level, double average) {
		this.userId = userId;
		this.subject = subject;
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
		averageForMeter = average * 10;
		return averageForMeter;
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
	
}
