package com.trainingquizzes.english.dto;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trainingquizzes.english.model.Quest;
import com.trainingquizzes.english.model.Score;
import com.trainingquizzes.english.model.User;

public class QuestDto {

	private Long id;
	private String title;
	private SubjectWithoutUserDto subject;
	private ChronoUnit timeUnit;
	private long timeInterval;
	private String startDate;
	private String finishDate;
	private String timeZone;
	private boolean finished;
	private List<UserDto> subscribedUsers;
	private List<TrialDto> trials;
	private List<Score> scores;
	private UserDto user;
	private String pin;
	
	public QuestDto(Quest quest, List<User> subscribedUsers) {
		this.id = quest.getId();
		this.title = quest.getTitle();
		this.setSubject(new SubjectWithoutUserDto(quest.getSubject()));
		this.timeUnit = quest.getTimeUnit();
		this.timeInterval = quest.getTimeInterval();
		this.startDate = quest.getStartDate().toOffsetDateTime().toString();
		this.finishDate = quest.getFinishDate().toOffsetDateTime().toString();
		this.timeZone = quest.getTimeZone();
		this.finished = quest.isFinished();
		this.subscribedUsers = subscribedUsers.stream().map(UserDto::new).collect(Collectors.toList());
		this.trials = quest.getTrials().stream().map(TrialDto::new).collect(Collectors.toList());
		this.scores = quest.getScores();
		this.user = new UserDto(quest.getUser());
		this.pin = quest.getPin();
	}
	
	public QuestDto(Quest quest) {
		this.id = quest.getId();
		this.title = quest.getTitle();
		this.setSubject(new SubjectWithoutUserDto(quest.getSubject()));
		this.timeUnit = quest.getTimeUnit();
		this.timeInterval = quest.getTimeInterval();
		this.startDate = quest.getStartDate().toLocalDateTime().toString();
		this.finishDate = quest.getFinishDate().toLocalDateTime().toString();
		this.timeZone = quest.getTimeZone();
		this.finished = quest.isFinished();
		this.trials = quest.getTrials().stream().map(TrialDto::new).collect(Collectors.toList());
		this.scores = quest.getScores();
		this.user = new UserDto(quest.getUser());
		this.pin = quest.getPin();
	}

	public static Page<QuestDto> convertToPageable(Page<Quest> quests, Map<Long, List<User>> subscribedUsers) {
		return quests.map(quest -> new QuestDto(quest, subscribedUsers.get(quest.getId())));
	}
	
	public static List<QuestDto> convertToList(List<Quest> quests) {
		return quests.stream().map(QuestDto::new).collect(Collectors.toList());
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ChronoUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(ChronoUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public long getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public List<TrialDto> getTrials() {
		return trials;
	}

	public void setTrials(List<TrialDto> trials) {
		this.trials = trials;
	}

	public List<Score> getScores() {
		return scores;
	}

	public void setScores(List<Score> scores) {
		this.scores = scores;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<UserDto> getSubscribedUsers() {
		return subscribedUsers;
	}

	public void setSubscribedUsers(List<UserDto> subscribedUsers) {
		this.subscribedUsers = subscribedUsers;
	}

	@JsonProperty("subject")
	public SubjectWithoutUserDto getSubject() {
		return subject;
	}

	public void setSubject(SubjectWithoutUserDto subject) {
		this.subject = subject;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public String getPin() {
		return pin;
	}

	public String getTimeZone() {
		return timeZone;
	}

}
