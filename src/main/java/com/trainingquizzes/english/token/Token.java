package com.trainingquizzes.english.token;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.trainingquizzes.english.enums.Roles;

@MappedSuperclass
public abstract class Token {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	protected String token;
	protected String email;
	protected String password;
	protected Date expiryDate;
	protected Roles role;
	
	public Long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasword() {
		return password;
	}

	public void setPasword(String pasword) {
		this.password = pasword;
	}

	public void setExpiryDate() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		date = calendar.getTime();
		this.expiryDate = date;
	}
	
	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

}
