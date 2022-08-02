package com.trainingquizzes.english.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.trainingquizzes.english.model.TemporaryTrialDataStore;

public class TrialTasksDto {
	
	private Long id;
	private List<TaskDto> tasks;
	private int tasksIndex;
	private double score;
	private boolean finished;
	
	public TrialTasksDto(TemporaryTrialDataStore trialItem) {
		this.id = trialItem.getId();
		this.tasks = trialItem.getReducedTasksList().stream().map(TaskDto::new).collect(Collectors.toList());
		this.tasksIndex = trialItem.getTasksIndex();
		this.score = trialItem.getScore();
		this.finished = trialItem.isFinished();
	}

	public List<TaskDto> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskDto> tasks) {
		this.tasks = tasks;
	}

	public int getTasksIndex() {
		return tasksIndex;
	}

	public void setTasksIndex(int tasksIndex) {
		this.tasksIndex = tasksIndex;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Long getId() {
		return id;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

}
