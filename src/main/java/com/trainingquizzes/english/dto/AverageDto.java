package com.trainingquizzes.english.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Average;

public class AverageDto {
	
	private String subjectTitle;
	private LevelType level;
	private double average;
	
	public AverageDto(Average average) {
		this.subjectTitle = average.getSubject().getTitle();
		this.level = average.getLevel();
		this.average = average.getAverage();
	}
	
	public String getSubjectTitle() {
		return subjectTitle;
	}
	
	public LevelType getLevel() {
		return level;
	}
	
	public double getAverage() {
		return average;
	}
	
	public static List<AverageDto> convertList(List<Average> averages) {
		return averages.stream().map(AverageDto::new).collect(Collectors.toList());
	}

	
}
