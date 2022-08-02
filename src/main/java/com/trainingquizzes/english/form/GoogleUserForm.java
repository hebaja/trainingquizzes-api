package com.trainingquizzes.english.form;

import java.util.List;

import com.trainingquizzes.english.enums.Roles;

public class GoogleUserForm {

	private String idToken;
	private List<Roles> roles;

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}

}
