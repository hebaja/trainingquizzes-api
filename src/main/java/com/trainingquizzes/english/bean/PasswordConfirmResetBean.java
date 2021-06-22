package com.trainingquizzes.english.bean;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.PasswordResetTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.token.PasswordResetToken;
import com.trainingquizzes.english.token.UserRegisterToken;
import com.trainingquizzes.english.util.EmailSender;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Controller
@ViewScoped
public class PasswordConfirmResetBean {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordResetTokenRepository resetTokenRepository;
	
	private User user;
	private String token;
	private String newPassword;
	private Boolean changePasswordFormRender = false;
	private Boolean loginButtonRender = false;
	private PasswordResetToken passwordResetToken;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public Boolean getChangePasswordFormRender() {
		return changePasswordFormRender;
	}

	public void setChangePasswordFormRender(Boolean changePasswordFormRender) {
		this.changePasswordFormRender = changePasswordFormRender;
	}
	
	public Boolean getLoginButtonRender() {
		return loginButtonRender;
	}

	public void setLoginButtonRender(Boolean loginButtonRender) {
		this.loginButtonRender = loginButtonRender;
	}

	public void token() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		setToken(request.getParameter("token"));
		
		passwordResetToken = resetTokenRepository.findByToken(token);
		Date date = new Date();
		
		if(passwordResetToken != null && passwordResetToken.getExpiryDate().after(date)) {
			setChangePasswordFormRender(true);
			user = passwordResetToken.getUser();
		} else {
			showErrorMessage(facesContext, "Link is broken or has expired.");
		}
		
	}

	public void reset() {
		String passwordHashString = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
		user.setPassword(passwordHashString);
		FacesContext facesContext = FacesContext.getCurrentInstance();

		try {
			updateUser();
			redirectMessage(facesContext);
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage(facesContext, "There was a problem trying to change password. Please try again.");
		}
	}
	
	public void androidReset() {
		String passwordHashString = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
		user.setPassword(passwordHashString);
		FacesContext facesContext = FacesContext.getCurrentInstance();

		try {
			updateUser();
			facesContext.addMessage(null, new FacesMessage("Password successfully altered."));
		} catch (Exception e) {
			e.printStackTrace();
			facesContext.addMessage(null, new FacesMessage("There was a problem when trying to alter password."));
		}
	}

	private void redirectMessage(FacesContext facesContext) throws IOException {
		facesContext.addMessage(null, new FacesMessage("Password successfully altered."));
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
		facesContext.getExternalContext().redirect("/login");
	}

	private void updateUser() {
		userRepository.save(user);
		resetTokenRepository.delete(passwordResetToken);
	}
	
	private void showErrorMessage(FacesContext facesContext, String message) {
		FacesMessage facesMessage = new FacesMessage(message);
		facesContext.addMessage(null, facesMessage);
	}
	
}
