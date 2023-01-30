package com.trainingquizzes.english.dto;

public class FirebaseTrialMessageDto {

	private long questId;
	private long userId;
	private long trialId;
	private int trialNumber;

	public FirebaseTrialMessageDto(long questId, long userId, long trialId, int trialNumber) {
		this.questId = questId;
		this.userId = userId;
		this.trialId = trialId;
		this.trialNumber = trialNumber;
	}

	public long getQuestId() {
		return questId;
	}

	public long getUserId() {
		return userId;
	}

	public long getTrialId() {
		return trialId;
	}

	public int getTrialNumber() {
		return trialNumber;
	}

}
