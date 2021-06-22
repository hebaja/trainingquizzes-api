package com.trainingquizzes.english.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.model.Account;
import com.trainingquizzes.english.model.Authority;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.UserRegisterTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.token.UserRegisterToken;

@Controller
@ViewScoped
public class UserConfirmRegisterBean {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRegisterTokenRepository registerTokenRepository;
	
	private FacesContext facesContext = FacesContext.getCurrentInstance();
	private String token;
	private String androidToken;
	private Boolean loginRender = false;
	private Boolean successRegisterRender = false;
	private User newUser;
	private String messagesColor;
	
	public String getToken() {
		return token;
	}
	
	public String getAndroidToken() {
		return androidToken;
	}

	public void setAndroidToken(String androidToken) {
		this.androidToken = androidToken;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getLoginRender() {
		return loginRender;
	}

	public void setLoginRender(Boolean loginRender) {
		this.loginRender = loginRender;
	}
	
	public Boolean getSuccessRegisterRender() {
		return successRegisterRender;
	}

	public void setSuccessRegisterRender(Boolean successRegisterRender) {
		this.successRegisterRender = successRegisterRender;
	}

	public String getMessagesColor() {
		return messagesColor;
	}

	public void setMessagesColor(String messagesColor) {
		this.messagesColor = messagesColor;
	}

	public void token() throws IOException {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
		        .getRequest();

		setToken(request.getParameter("token"));
		
		UserRegisterToken userRegisterToken = registerTokenRepository.findByToken(token);
		Date date = new Date();
		
		if(userRegisterToken != null && userRegisterToken.getExpiryDate().after(date)) {
			createUser(userRegisterToken);
			try {
				saveUser(userRegisterToken, newUser);
				redirectWithMessage("User " + newUser.getUsername() + " was successfully registered. You can login now.");
				
			} catch (Exception e) {
				e.printStackTrace();
				redirectWithMessage("There was a problem trying to register user. Please try again.");
			}
		} else {
			redirectWithMessage("User registration token is broken or has expired.");
		}
		
	}
	
	public void androidToken() throws IOException {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
		        .getRequest();
		
		FacesContext context = FacesContext.getCurrentInstance();

		setAndroidToken(request.getParameter("token"));
		
		UserRegisterToken userRegisterToken = registerTokenRepository.findByToken(androidToken);
		Date date = new Date();
		
		if(userRegisterToken != null && userRegisterToken.getExpiryDate().after(date)) {
			createUser(userRegisterToken);
			try {
				saveUser(userRegisterToken, newUser);
		        context.addMessage(null, new FacesMessage("User " + userRegisterToken.getUsername() + " was successfully registered"));
		        setMessagesColor("text-success");
		        setSuccessRegisterRender(true);
			} catch (Exception e) {
				e.printStackTrace();
				setMessagesColor("text-danger");
				context.addMessage(null, new FacesMessage("There was a problem trying to register user."));
			}
		} else {
			setMessagesColor("text-danger");
			context.addMessage(null, new FacesMessage("User registration token is broken or has expired."));
		}
		
	}

	private void createUser(UserRegisterToken userRegisterToken) throws IOException {
		List<Account> accounts = new ArrayList<>();
		accounts.add(new Account(AccountType.EMAIL));
		
		Authority authority = new Authority(Roles.ROLE_USER);
		List<Authority> roles = Arrays.asList(authority);
		
		String uid = RandomStringUtils.random(18, "0123456789");
		
		newUser = new User(userRegisterToken.getUsername(), userRegisterToken.getEmail(),
				userRegisterToken.getPasword(), true, roles, accounts);
		
		newUser.setUid(uid);
	}

	private void redirectWithMessage(String message) throws IOException {
		facesContext.addMessage(null, new FacesMessage(message));
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
		facesContext.getExternalContext().invalidateSession();
		facesContext.getExternalContext().redirect("/login");
	}

	private void saveUser(UserRegisterToken userRegisterToken, User newUser) {
		userRepository.save(newUser);
		registerTokenRepository.delete(userRegisterToken);
	}
	
}
