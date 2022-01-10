package com.trainingquizzes.english.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.model.Account;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.model.UserRole;
import com.trainingquizzes.english.repository.UserRegisterTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.token.UserRegisterToken;
import com.trainingquizzes.english.util.EmailSender;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Controller
@ViewScoped
public class UserRegisterBean {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRegisterTokenRepository registerTokenRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring-english-training-quizzes-default-domain}")
	private String defaultDomain;
	
	private User user = new User();
	
	private String confirmEmail;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
		
	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}
		
	public void register() throws IOException {
		
		String token = UUID.randomUUID().toString();
		
		List<UserRole> roles = new ArrayList<>();
		roles.add(new UserRole(Roles.ROLE_USER));
		
		List<Account> accounts = new ArrayList<>();
		accounts.add(new Account(AccountType.EMAIL));
		
		String passwordHashString = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
		
		UserRegisterToken userToRegisterToken = new UserRegisterToken(token, user.getUsername(), user.getEmail(), passwordHashString);
		userToRegisterToken.setExpiryDate();
		
		registerTokenRepository.save(userToRegisterToken);
		
		EmailSender emailSender = new EmailSender(javaMailSender);
		
		emailSender.sendMail(userToRegisterToken, 
				user.getEmail(), 
				"Complete user registration", 
				"To complete user registration, please click here: "
						+ defaultDomain
						+ "user/confirm-register?token=" 
						+ userToRegisterToken.getToken() 
						+ " (This link will expire after 24 hours).");
		
		redirectToLoginPage();
		setUser(new User());
		setConfirmEmail(null);
	}

	private void redirectToLoginPage() throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage("An e-mail has been sent to " + user.getEmail() + ". Check your inbox and access the link to complete registration."));
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
		facesContext.getExternalContext().redirect("/login");
	}
	
	public void token() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
		        .getRequest();

		String token = request.getParameter("token");
		System.out.println(token);
	}
	
	public void validateEmail(FacesContext context, UIComponent component, Object value) {
		boolean emailExists = userRepository.existsByEmail((String)value);
		if(emailExists) {
			((UIInput) component).setValid(false);
			FacesMessage message = new FacesMessage("This e-mail has already been registered");
			context.addMessage(component.getClientId(context), message);
		}
	}
}
