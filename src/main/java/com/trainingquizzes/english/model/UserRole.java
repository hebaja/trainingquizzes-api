package com.trainingquizzes.english.model;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.trainingquizzes.english.enums.Roles;

@Embeddable
public class UserRole {
	
	@Enumerated(EnumType.STRING)
	private Roles role;
	
	public UserRole() {}

	public UserRole(Roles role) {
		this.role = role;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

}
