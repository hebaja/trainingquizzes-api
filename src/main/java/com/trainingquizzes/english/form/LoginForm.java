package com.trainingquizzes.english.form;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.trainingquizzes.english.enums.Roles;

public class LoginForm {

	private String email;
	private String password;
	private List<Roles> roles;

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String senha) {
		this.password = senha;
	}

	public String getEmail() {
		return email;
	}

	public String getPassoword() {
		return password;
	}

	public UsernamePasswordAuthenticationToken convert() {
		return new UsernamePasswordAuthenticationToken(email, password);
	}

	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}

}
