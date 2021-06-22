package com.trainingquizzes.english.model;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.trainingquizzes.english.enums.Roles;

@Embeddable
@Table(name = "authorities")
public class Authority {
	
	@Enumerated(EnumType.STRING)
	private Roles role;
	
	public Authority() {}
	
	public Authority(Roles role) {
		this.role = role;
	}
	
	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

}
