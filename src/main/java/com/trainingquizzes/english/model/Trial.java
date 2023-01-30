package com.trainingquizzes.english.model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


@Entity
public class Trial {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int trialNumber;
	private Double score;
	private ZonedDateTime startDate;
	private ZonedDateTime finishDate;
	private boolean finished;
	
	@OneToOne
	@Cascade(CascadeType.DETACH)
	private User subscribedUser;
		
	@OneToOne
	@Cascade(CascadeType.DETACH)
	private Subject subject;
	
	@ManyToOne
	private Quest quest;
	
	public Trial() {}

	public Trial(int trialNumber, User subscribedUser, Quest quest, ZonedDateTime startDate, ZonedDateTime finishDate) {
		this.trialNumber = trialNumber;
		this.subscribedUser = subscribedUser;
		this.quest = quest;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.finished = false;
	}

	public User getSubscribedUser() {
		return subscribedUser;
	}

	public void setSubscribedUser(User subscribedUser) {
		this.subscribedUser = subscribedUser;
	}

	public void setFinishDate(ZonedDateTime finishDate) {
		this.finishDate = finishDate;
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

	public boolean setScore(Double score) {
		if(ZonedDateTime.now().isBefore(finishDate)) {
			this.score = score;
			return true;
		}
		return false;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public ZonedDateTime getFinishDate() {
		return finishDate;
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

	public Quest getQuest() {
		return quest;
	}

}
