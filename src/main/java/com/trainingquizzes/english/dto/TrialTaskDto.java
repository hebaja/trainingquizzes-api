package com.trainingquizzes.english.dto;

import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.model.TemporaryTrialDataStore;

public class TrialTaskDto {
	
	private Long id;
	private Long trialId;
	private Long questId;
	private TaskDto task;
	private int tasksIndex;
	private double score;
	private boolean finished;
	
	public TrialTaskDto(TemporaryTrialDataStore temporaryTrialDataStore, Task receivedTask) {
		this.id = temporaryTrialDataStore.getId();
		this.trialId = temporaryTrialDataStore.getTrial().getId();
		this.questId = temporaryTrialDataStore.getTrial().getQuest().getId();
		if(!temporaryTrialDataStore.isFinished()) this.task = new TaskDto(receivedTask);
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

	public Long getTrialId() {
		return trialId;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}

	public Long getQuestId() {
		return questId;
	}

	public void setQuestId(Long questId) {
		this.questId = questId;
	}

}
