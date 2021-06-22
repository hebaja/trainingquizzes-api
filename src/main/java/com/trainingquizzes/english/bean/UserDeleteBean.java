package com.trainingquizzes.english.bean;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.PasswordResetTokenRepository;
import com.trainingquizzes.english.repository.UserRegisterTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.token.PasswordResetToken;
import com.trainingquizzes.english.token.UserRegisterToken;

@Controller
@ViewScoped
public class UserDeleteBean {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRegisterTokenRepository registerTokenRepository;
	
	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;

	private Boolean returnToAppRender = false;
	
	private Boolean deleteAccountButtonRender = true;
	
	public Boolean getReturnToAppRender() {
		return returnToAppRender;
	}

	public void setReturnToAppRender(Boolean returnToAppRender) {
		this.returnToAppRender = returnToAppRender;
	}

	public Boolean getDeleteAccountButtonRender() {
		return deleteAccountButtonRender;
	}

	public void setDeleteAccountButtonRender(Boolean deleteAccountButtonRender) {
		this.deleteAccountButtonRender = deleteAccountButtonRender;
	}

	public void delete() throws IOException {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		User user = fetchUser();
		if(user != null) {
			removeUser(user);
			redirectWebMessage(facesContext, user);
		} else {
			showErrorMessage(facesContext);
		}
	}

	public void androidDelete() throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		User user = fetchUser();
		if(user != null) {
			removeUser(user);
			removedUserMessage(facesContext, user);
			setReturnToAppRender(true);
			setDeleteAccountButtonRender(false);
		} else {
			showErrorMessage(facesContext);
		}
	}
	
	private User fetchUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userInfo = auth.getName();
		User user = userRepository.findByEmail(userInfo)
						.orElse(userRepository.findByUid(userInfo)
						.orElse(null));
		return user;
	}

	private void showErrorMessage(FacesContext facesContext) {
		FacesMessage facesMessage = new FacesMessage("There was a problem and user was not removed. Please try again.");
		facesContext.addMessage(null, facesMessage);
	}
	
	private void removedUserMessage(FacesContext facesContext, User user) {
		FacesMessage facesMessage = new FacesMessage("User " + user.getUsername() + " was successfully removed");
		facesContext.addMessage(null, facesMessage);
	}

	private void redirectWebMessage(FacesContext facesContext, User user) throws IOException {
		SecurityContextHolder.getContext().setAuthentication(null);
		facesContext.addMessage(null, new FacesMessage("User " + user.getUsername() + " was successfully deleted"));
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
		facesContext.getExternalContext().invalidateSession();
		facesContext.getExternalContext().redirect("/login");
	}

	private void removeUser(User user) {
		List<PasswordResetToken> passwordResetTokens = passwordTokenRepository.findAllByUserId(user.getId());
		if(passwordResetTokens != null) {
			passwordTokenRepository.deleteAll(passwordResetTokens);
		}
		
		List<UserRegisterToken> userRegisterTokens = registerTokenRepository.findAllByEmail(user.getEmail());
		if(userRegisterTokens != null) {
			registerTokenRepository.deleteAll(userRegisterTokens);
		}
		
		userRepository.delete(user);
	}
	
}
