package com.trainingquizzes.english.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.trainingquizzes.english.model.Trial;

public class TrialDto {

	private Long id;
	private SubjectDto subject;
	private UserDto user;
	private int trialNumber;
	private Double score;
	private LocalDateTime startDate;
	private LocalDateTime finishDate;
	private boolean finished;
	
	public TrialDto(Trial trial) {
		this.id = trial.getId();
		this.setSubject(new SubjectDto(trial.getQuest().getSubject()));
		this.user = new UserDto(trial.getSubscribedUser());
		this.trialNumber = trial.getTrialNumber();
		this.score = trial.getScore();
		this.startDate = trial.getStartDate();
		this.finishDate = trial.getFinishDate();
		this.finished = trial.isFinished();
	}
	
	public static List<TrialDto> convertList(List<Trial> trials) {
		return trials.stream().map(TrialDto::new).collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTrialNumber() {
		return trialNumber;
	}

	public void setTrialNumber(int trialNumber) {
		this.trialNumber = trialNumber;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(LocalDateTime finishDate) {
		this.finishDate = finishDate;
	}

	public SubjectDto getSubject() {
		return subject;
	}

	public void setSubject(SubjectDto subject) {
		this.subject = subject;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

}
