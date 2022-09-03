package com.trainingquizzes.english.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.google.common.util.concurrent.AtomicDouble;

@Entity
public class Quest implements Cloneable {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	ChronoUnit timeUnit;
	long timeInterval;
	private LocalDateTime startDate;
	private LocalDateTime finishDate;
	private boolean finished = false;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Subject subject;
	
	@ElementCollection
	private Map<String, Double> result = new HashMap<>();
	
	@OneToMany(mappedBy = "quest", cascade = CascadeType.ALL)
	private List<Trial> trials = new ArrayList<>();
	
	@ElementCollection
	private List<Long> subscribedUsersIds = new ArrayList<>();
	
	public Quest() {}

	public Quest(String title, User user, Subject subject, LocalDateTime startDate, LocalDateTime finishDate, long timeInterval, ChronoUnit timeUnit, List<Long> subscribedUsersIds) {
		this.title = title;
		this.user = user;
		this.subject = subject;
		this.setStartDate(startDate);
		this.setFinishDate(finishDate);
		this.setTimeInterval(timeInterval);
		this.timeUnit = timeUnit;
		this.subscribedUsersIds = subscribedUsersIds;
	}
	
	public Quest(String title, User user, Subject subject, LocalDateTime startDate, LocalDateTime finishDate, long timeInterval, ChronoUnit timeUnit) {
		this.title = title;
		this.user = user;
		this.subject = subject;
		this.setStartDate(startDate);
		this.setFinishDate(finishDate);
		this.setTimeInterval(timeInterval);
		this.timeUnit = timeUnit;
	}
	
	public void subscribeUser(User subscribedUser) {
		this.subscribedUsersIds.add(subscribedUser.getId());
		generateSubscribedUserTrials(subscribedUser);
	}

	private void generateSubscribedUserTrials(User subscribedUser) {
		int trialNumber = 1;
		
		long trialDurationInMilliseconds = (ChronoUnit.MILLIS.between(startDate, finishDate)) / timeInterval;
		
		LocalDateTime trialStartDate = this.startDate;
		LocalDateTime trialFinishDate = incrementTrialDuration(trialStartDate, trialDurationInMilliseconds);
		
		while(trialStartDate.isBefore(finishDate)) {
			Trial trial = generateTrial(trialNumber, subscribedUser, trialStartDate, trialFinishDate);
			this.getTrials().add(trial);
			trialStartDate = incrementTrialDuration(trialStartDate, trialDurationInMilliseconds);
			trialFinishDate = incrementTrialDuration(trialFinishDate, trialDurationInMilliseconds);
			trialNumber++;
		}
	}

	private LocalDateTime incrementTrialDuration(LocalDateTime localDateTime, long trialDurationInMilliseconds) {
		return localDateTime.plus(trialDurationInMilliseconds, ChronoUnit.MILLIS);
	}

	private Trial generateTrial(int trialNumber, User subscribedUser, LocalDateTime trialStartDate, LocalDateTime trialFinishDate) {
		return new Trial(
				trialNumber,
				subscribedUser,
				this,
				trialStartDate, 
				subtractSomeMillisFrom(trialFinishDate));
	}

	private LocalDateTime subtractSomeMillisFrom(LocalDateTime localDateTime) {
		return localDateTime.minus(50, ChronoUnit.MILLIS);
	}
	
	public void finishResult() {
		subscribedUsersIds.forEach(userId -> {
			double score = 0;
			for (Trial trial : trials) {
				if(trial.getSubscribedUser().getId().equals(userId) && trial.getScore() != null) {
					score += trial.getScore();
				}
			}
			String formatKey = user.getUsername() + " (" + user.getEmail() + ")"; 
			result.put(formatKey, score / timeInterval);
		});
	}
	
	public void unsubscribeUser(User subscribedUser) {
		this.subscribedUsersIds.remove(subscribedUser.getId());
		removeSubscribedUserTrials(subscribedUser);
	}

	private void removeSubscribedUserTrials(User subscribedUser) {
		this.getTrials().removeIf(trial -> trial.getSubscribedUser().equals(subscribedUser));
	}

	public Map<String, Double> getResult() {
		return result;
	}

	public void setResult(Map<String, Double> result) {
		this.result = result;
	}

	public List<Trial> getTrials() {
		return trials;
	}

	public void setTrials(List<Trial> trials) {
		this.trials = trials;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public LocalDateTime getFinishDate() {
		return finishDate;
	}

	public ChronoUnit getTimeUnit() {
		return timeUnit;
	}

	public long getTimeInterval() {
		return timeInterval;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void setFinishDate(LocalDateTime finishDate) {
		this.finishDate = finishDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	public List<Long> getSubscribedUsersIds() {
		return subscribedUsersIds;
	}

	public void setSubscribedUsersIds(List<Long> subscribedUsersIds) {
		this.subscribedUsersIds = subscribedUsersIds;
	}

	private long getScoresDivisor() {
		long totalLengthOfQuestMillis = ChronoUnit.MILLIS.between(startDate, finishDate);
		long totalLengthOfEachTrialMillis = totalLengthOfQuestMillis / timeInterval;
		long scoresDivider = 0;
		
		if(isFinished()) {
			scoresDivider = timeInterval;
		} else {
			while(scoresDivider < timeInterval) {
				LocalDateTime trialLimitDate = startDate.plus(totalLengthOfEachTrialMillis * (scoresDivider), ChronoUnit.MILLIS);
				if(trialLimitDate.isBefore(LocalDateTime.now())) {
					scoresDivider++;
				} else {
					break;
				}
			}
		}
						
		return scoresDivider;
	}
	
	public List<Score> getScores() {
		ArrayList<Score> scores = new ArrayList<>();
		
		Map<String, List<Trial>> scoresMap = trials.stream().collect(Collectors.groupingBy(trial -> trial.getSubscribedUser().getFormattedUserAndEmail())); 
		
		scoresMap.forEach((username, mappedTrials) -> {
			AtomicDouble score = new AtomicDouble(0.0);
			
			mappedTrials.forEach(trial -> {
				if(trial.getScore() != null) {
					score.addAndGet(trial.getScore());
				}
			});
			if(score.get() > 0) scores.add(new Score(username, mappedTrials.get(0).getSubscribedUser().getPictureUrl(), score.get() / getScoresDivisor())); 
		});
						
		return scores.stream().sorted((s1, s2) -> Double.compare(s2.getScore(), s1.getScore())).collect(Collectors.toList());
	}

}
