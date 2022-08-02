package com.trainingquizzes.english.form;

import java.util.List;

import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.model.User;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UserForm {
	
	private Long id;
	private String username;
	private String email;
	private String password;
	private List<Roles> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public User convert() {
		User teacher = new User();
		
		String passwordHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
		
		teacher.setUsername(username);
		teacher.setEmail(email);
		teacher.setPassword(passwordHashString);
		
		return teacher;
	}

	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}
}
