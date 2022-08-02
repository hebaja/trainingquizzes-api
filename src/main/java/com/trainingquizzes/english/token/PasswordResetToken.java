package com.trainingquizzes.english.token;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.trainingquizzes.english.model.User;

@Entity
public class PasswordResetToken extends Token {
	
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

	public PasswordResetToken(String token, User user) {
		this.token = token;
		this.user = user;
	}
	
	@Deprecated
	public PasswordResetToken() {}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
