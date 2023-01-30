package com.trainingquizzes.english.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Average;

public class AverageDto {
	
	private String uid;
	private String subjectTitle;
	private UserDto user;
	private LevelType level;
	private double average;
	private double averageForMeter;
	private String levelCapitalize;
	
	public AverageDto(Average average) {
		this.uid = UUID.randomUUID().toString();
		this.subjectTitle = average.getSubject().getTitle();
		this.setUser(new UserDto(average.getUser()));
		this.level = average.getLevel();
		this.average = average.getAverage();
		this.averageForMeter = average.getAverageForMeter();
		this.levelCapitalize = average.getLevelCapitalize();
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

	public double getAverageForMeter() {
		return averageForMeter;
	}

	public String getLevelCapitalize() {
		return levelCapitalize;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uuid) {
		this.uid = uuid;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public static Page<AverageDto> convertToPageable(Page<Average> averages) {
		return averages.map(AverageDto::new);
	}
}
