package com.trainingquizzes.english.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.model.Account;
import com.trainingquizzes.english.model.Authority;
import com.trainingquizzes.english.model.User;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class NewUserRequest {

	@Size(min = 4, max = 20)
	private String username;
	
	@Email
	@NotBlank
	private String email;
	
	@Email
	@NotBlank
	private String matchingEmail;
	
	@Size(min = 4, max = 20)
	@NotBlank
	private String password;
    
    @Size(min = 4, max = 20)
	@NotBlank
    private String matchingPassword;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String userName) {
		this.username = userName;
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
	
	public String getMatchingPassword() {
		return matchingPassword;
	}
	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}
	
	public String getMatchingEmail() {
		return matchingEmail;
	}
	public void setMatchingEmail(String matchingEmail) {
		this.matchingEmail = matchingEmail;
	}
	
	public User toUser() {
		String passwordHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
		Set<Account> accounts = new HashSet<>();
		accounts.add(new Account(AccountType.EMAIL));
		Set<Authority> roles = new HashSet<>();
		roles.add(new Authority(Roles.ROLE_TEACHER));
		return new User(username, email, passwordHashString, false, roles, accounts);
	}
}
