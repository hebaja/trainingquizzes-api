package com.trainingquizzes.english.form;

import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.trainingquizzes.english.enums.Roles;

public class LoginForm {

	private String email;
	private String password;
	private Set<Roles> roles;

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

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

}
