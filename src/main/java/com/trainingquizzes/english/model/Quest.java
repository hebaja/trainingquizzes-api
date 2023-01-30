package com.trainingquizzes.english.model;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.common.util.concurrent.AtomicDouble;

@Entity
public class Quest implements Cloneable {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private ChronoUnit timeUnit;
	long timeInterval;
	private ZonedDateTime startDate;
	private ZonedDateTime finishDate;
	private boolean finished = false;
	private String timeZone;
	
	@Column(unique = true)
	private String pin;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Subject subject;
	
	@OneToMany(mappedBy = "quest", cascade = CascadeType.ALL)
	private List<Trial> trials = new ArrayList<>();
	
	@ElementCollection
	private List<Long> subscribedUsersIds = new ArrayList<>();
	
	public Quest() {}

	public Quest(String title, User user, Subject subject, ZonedDateTime startDate, ZonedDateTime finishDate, String timeZone, long timeInterval, ChronoUnit timeUnit, List<Long> subscribedUsersIds) {
		this.title = title;
		this.user = user;
		this.subject = subject;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.timeZone = timeZone;
		this.timeInterval = timeInterval;
		this.timeUnit = timeUnit;
		this.subscribedUsersIds = subscribedUsersIds;
		generatePin();
	}
	
	public Quest(String title, User user, Subject subject, ZonedDateTime startDate, ZonedDateTime finishDate, String timeZone, long timeInterval, ChronoUnit timeUnit) {
		this.title = title;
		this.user = user;
		this.subject = subject;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.timeZone = timeZone;
		this.timeInterval = timeInterval;
		this.timeUnit = timeUnit;
		generatePin();
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

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public ZonedDateTime getFinishDate() {
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

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void setFinishDate(ZonedDateTime finishDate) {
		this.finishDate = finishDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}
	
	public String getPin() {
		return pin;
	}
	
	public void setPin(String pin) {
		this.pin = pin;
	}

	public List<Long> getSubscribedUsersIds() {
		return subscribedUsersIds;
	}

	public void setSubscribedUsersIds(List<Long> subscribedUsersIds) {
		this.subscribedUsersIds = subscribedUsersIds;
	}
	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
	public long getTrialsLength() {
		return ChronoUnit.MILLIS.between(startDate, finishDate) / timeInterval;
	}
	
	public void subscribeUser(User subscribedUser) {
		this.subscribedUsersIds.add(subscribedUser.getId());
		generateSubscribedUserTrials(subscribedUser);
	}

	private void generateSubscribedUserTrials(User subscribedUser) {
		int trialNumber = 1;
		
		ZonedDateTime trialStartDate = this.startDate;
		ZonedDateTime trialFinishDate = incrementTrialDuration(trialStartDate, getTrialsLength());
		
		while(trialStartDate.isBefore(finishDate)) {
			Trial trial = generateTrial(trialNumber, subscribedUser, trialStartDate, trialFinishDate);
			this.getTrials().add(trial);
			trialStartDate = incrementTrialDuration(trialStartDate, getTrialsLength());
			trialFinishDate = incrementTrialDuration(trialFinishDate, getTrialsLength());
			trialNumber++;
		}
	}

	private ZonedDateTime incrementTrialDuration(ZonedDateTime localDateTime, long trialDurationInMilliseconds) {
		return localDateTime.plus(trialDurationInMilliseconds, ChronoUnit.MILLIS);
	}

	private Trial generateTrial(int trialNumber, User subscribedUser, ZonedDateTime trialStartDate, ZonedDateTime trialFinishDate) {
		return new Trial(
				trialNumber,
				subscribedUser,
				this,
				trialStartDate, 
				subtractSomeMillisFrom(trialFinishDate));
	}

	private ZonedDateTime subtractSomeMillisFrom(ZonedDateTime localDateTime) {
		return localDateTime.minus(50, ChronoUnit.MILLIS);
	}
	
	public void unsubscribeUser(User subscribedUser) {
		this.subscribedUsersIds.remove(subscribedUser.getId());
		removeSubscribedUserTrials(subscribedUser);
	}

	private void removeSubscribedUserTrials(User subscribedUser) {
		this.getTrials().removeIf(trial -> trial.getSubscribedUser().equals(subscribedUser));
	}

	private long getScoresDivisor() {
		long scoresDivider = 0;
		
		if(isFinished()) {
			scoresDivider = timeInterval;
		} else {
			while(scoresDivider < timeInterval) {
				LocalDateTime localDateTime = startDate.toLocalDateTime().plus(getTrialsLength() * (scoresDivider), ChronoUnit.MILLIS);
				ZonedDateTime trialLimitDate = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
				ZonedDateTime currentZonedDateTime = ZonedDateTime.of(LocalDateTime.ofInstant(ZonedDateTime.now().toInstant(), ZoneOffset.UTC), ZoneId.of("UTC"));
				
				if(trialLimitDate.isBefore(currentZonedDateTime)) {
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

	private void generatePin() {
		SecureRandom random = new SecureRandom();
		int num = random.nextInt(100000);
		this.pin = String.format("%05d", num);
	}

	public String getTimeZone() {
		return timeZone;
	}
	
}