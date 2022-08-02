package com.trainingquizzes.english.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class TemporaryTrialDataStore {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private Trial trial;
	
	@OneToOne
	@Cascade(CascadeType.DETACH)
	private User user;
	
	@ManyToMany
	@Cascade(CascadeType.DETACH)
	private List<Task> reducedTasksList;
	
	private Long questId;
	private int trialNumber;
	private int tasksIndex;
	private double score;
	private boolean finished = false;

	public TemporaryTrialDataStore() {}
	
	public TemporaryTrialDataStore(Trial trial, User user, int trialNumber, List<Task> reducedTasksList) {
		this.trial = trial;
		this.questId = trial.getQuest().getId();
		this.setUser(user);
		this.trialNumber = trialNumber;
		this.reducedTasksList = reducedTasksList;
		this.tasksIndex = 0;
		this.score = 0;
	}

	public void iterateTasksIndex() {
		this.tasksIndex++;
	}
	
	public Trial getTrial() {
		return trial;
	}

	public void setTrial(Trial quest) {
		this.trial = quest;
	}

	public int getTrialNumber() {
		return trialNumber;
	}

	public void setTrialNumber(int trialNumber) {
		this.trialNumber = trialNumber;
	}

	public List<Task> getReducedTasksList() {
		return reducedTasksList;
	}

	public void setReducedTasksList(List<Task> reducedTasksList) {
		this.reducedTasksList = reducedTasksList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getTasksIndex() {
		return tasksIndex;
	}

	public void setTasksIndex(int tasksIndex) {
		this.tasksIndex = tasksIndex;
	}

	public Long getId() {
		return id;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Long getQuestId() {
		return questId;
	}

	public void setQuestId(Long questId) {
		this.questId = questId;
	}

}
