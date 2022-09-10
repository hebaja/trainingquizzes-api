package com.trainingquizzes.english.dto;

import com.trainingquizzes.english.model.TemporaryTrialDataStore;

public class TrialTaskDto {
	
	private Long id;
	private TaskDto task;
	private int tasksIndex;
	private double score;
	private boolean finished;
	
	public TrialTaskDto(TemporaryTrialDataStore temporaryTrialDataStore) {
		this.id = temporaryTrialDataStore.getId();
		if(!temporaryTrialDataStore.isFinished()) this.task = new TaskDto(temporaryTrialDataStore.getReducedTasksList().get(temporaryTrialDataStore.getTasksIndex()));
		else this.task = null;
		this.tasksIndex = temporaryTrialDataStore.getTasksIndex();
		this.score = temporaryTrialDataStore.getScore();
		this.finished = temporaryTrialDataStore.isFinished();
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

	public TaskDto getTask() {
		return task;
	}

	public void setTask(TaskDto task) {
		this.task = task;
	}

}
