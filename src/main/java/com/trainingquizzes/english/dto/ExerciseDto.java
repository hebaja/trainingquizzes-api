package com.trainingquizzes.english.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Exercise;

public class ExerciseDto {

	private Long id;
	private Long subjectId;
	private String username;
	private LevelType level;
	private double score;
	
	public ExerciseDto(Exercise exercise) {
		this.id = exercise.getId();
		this.subjectId = exercise.getSubject().getId();
		this.username = exercise.getUser().getUsername();
		this.level = exercise.getLevel();
		this.score = exercise.getScore();
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public LevelType getLevel() {
		return level;
	}

	public double getScore() {
		return score;
	}
	
	public static ExerciseDto convert(Exercise exercise) {
		return new ExerciseDto(exercise);
	}
	
	public static List<ExerciseDto> convertList(List<Exercise> exercises) {
		return exercises.stream().map(ExerciseDto::new).collect(Collectors.toList());
	}

	public Long getSubjectId() {
		return subjectId;
	}

}
