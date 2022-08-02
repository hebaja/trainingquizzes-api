package com.trainingquizzes.english.dto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trainingquizzes.english.model.Quest;
import com.trainingquizzes.english.model.User;

public class QuestDto {

	private Long id;
	private String title;
	private SubjectWithoutUserDto subject;
	private ChronoUnit timeUnit;
	private long timeInterval;
	private LocalDateTime startDate;
	private LocalDateTime finishDate;
	private boolean finished;
	private List<UserDto> subscribedUsers;
	private List<TrialDto> trials;
	private Map<String, Double> results;
	private UserDto user;
	
	public QuestDto(Quest quest, List<User> subscribedUsers) {
		this.id = quest.getId();
		this.title = quest.getTitle();
		this.setSubject(new SubjectWithoutUserDto(quest.getSubject()));
		this.timeUnit = quest.getTimeUnit();
		this.timeInterval = quest.getTimeInterval();
		this.startDate = quest.getStartDate();
		this.finishDate = quest.getFinishDate();
		this.finished = quest.isFinished();
		this.subscribedUsers = subscribedUsers.stream().map(UserDto::new).collect(Collectors.toList());
		this.trials = quest.getTrials().stream().map(TrialDto::new).collect(Collectors.toList());
		this.results = quest.getResult();
		this.user = new UserDto(quest.getUser());
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

	public List<TrialDto> getTrials() {
		return trials;
	}

	public void setTrials(List<TrialDto> trials) {
		this.trials = trials;
	}

	public Map<String, Double> getResults() {
		return results;
	}

	public void setResults(Map<String, Double> result) {
		this.results = result;
	}

	public static Page<QuestDto> convertToPageable(Page<Quest> quests, Map<Long, List<User>> subscribedUsers) {
		return quests.map(quest -> new QuestDto(quest, subscribedUsers.get(quest.getId())));
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

//	public Set<UserWithoutSubjectsDto> getEnrolledUsers() {
//		return enrolledUsers;
//	}
//
//	public void setEnrolledUsers(Set<UserWithoutSubjectsDto> enrolledUsers) {
//		this.enrolledUsers = enrolledUsers;
//	}

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

//	public UserDto getUser() {
//		return user;
//	}
//
//	public void setUser(UserDto user) {
//		this.user = user;
//	}

}
