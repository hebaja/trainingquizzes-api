package com.trainingquizzes.english.model;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Authority other = (Authority) obj;
		return role == other.role;
	}

}
