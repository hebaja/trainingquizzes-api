package com.trainingquizzes.english.model;

import java.time.LocalDateTime;

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
	private LocalDateTime startDate;
	private LocalDateTime finishDate;
	private boolean finished;
	
	@OneToOne
	@Cascade(CascadeType.DETACH)
	private User subscribedUser;
		
	@OneToOne
	@Cascade(CascadeType.DETACH)
	private Subject subject;
	
	@ManyToOne
	private Quest quest;
	
//	@PreRemove
//	private void remove() {
//		this.quest.removeTrial(this);
//	}
	
	public Trial() {}

	public Trial(int trialNumber, User subscribedUser, Quest quest, LocalDateTime startDate, LocalDateTime finishDate) {
		this.trialNumber = trialNumber;
//		this.subject = subject;
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

	public void setFinishDate(LocalDateTime finishDate) {
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
		if(LocalDateTime.now().isBefore(finishDate)) {
			this.score = score;
			return true;
		}
		return false;
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

	public Long getId() {
		return id;
	}

//	public Subject getSubject() {
//		return subject;
//	}
//
//	public void setSubject(Subject subject) {
//		this.subject = subject;
//	}

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
