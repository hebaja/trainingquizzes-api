package com.trainingquizzes.english.token;

import javax.persistence.Entity;

@Entity
public class PasswordResetToken extends Token {

	private long userId;

	public PasswordResetToken(String token, long userId) {
		this.token = token;
		this.userId = userId;
	}
	
	@Deprecated
	public PasswordResetToken() {}

	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}

}
