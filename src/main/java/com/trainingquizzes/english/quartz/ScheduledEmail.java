package com.trainingquizzes.english.quartz;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class ScheduledEmail {

	private String senderEmail;
	private String recipientEmail;
	private String subject;
	private String body;
	private ZonedDateTime startTime;
	
	public ScheduledEmail(String senderEmail, String recipientEmail, String subject, String body, ZonedDateTime startTime) {
		this.senderEmail = senderEmail;
		this.recipientEmail = recipientEmail;
		this.subject = subject;
		this.body = body;
		this.startTime = startTime;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

}
