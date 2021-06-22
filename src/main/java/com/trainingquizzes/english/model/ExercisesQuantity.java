package com.trainingquizzes.english.model;

import com.trainingquizzes.english.enums.LevelType;

public class ExercisesQuantity {

	private User user;
	private LevelType level;
	private String subject;
	private long quantity;

	public ExercisesQuantity(User user, LevelType level, String subject, long quantity) {
		this.user = user;
		this.level = level;
		this.subject = subject;
		this.quantity = quantity;
	}

	public long getQuantity() {
		return quantity;
	}

	public LevelType getLevel() {
		return level;
	}

	public String getSubject() {
		return subject;
	}

	public User getUser() {
		return user;
	}
}
