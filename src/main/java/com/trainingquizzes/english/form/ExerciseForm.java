package com.trainingquizzes.english.form;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Exercise;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.User;

public class ExerciseForm {

	private Long userId;
	private long subjectId;
	private LevelType level;
	private double score;
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userUid) {
		this.userId = userUid;
	}
	
	public long getSubjectId() {
		return subjectId;
	}
	
	public void setSubject(long subjectId) {
		this.subjectId = subjectId;
	}
	
	public LevelType getLevel() {
		return level;
	}
	
	public void setLevel(LevelType level) {
		this.level = level;
	}
	
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Exercise convert(User user, Subject subject) {
		return new Exercise(user, subject, level, score);
	}

}
