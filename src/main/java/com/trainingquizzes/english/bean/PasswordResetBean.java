package com.trainingquizzes.english.bean;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.PasswordResetTokenRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.token.PasswordResetToken;
import com.trainingquizzes.english.util.EmailSender;

@Controller
@ViewScoped
public class PasswordResetBean {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordResetTokenRepository resetTokenRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring-english-training-quizzes-default-domain}")
	private String defaultDomain;
	
	private String email;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void validateEmail(FacesContext context, UIComponent component, Object value) {
		boolean emailExists = userRepository.existsByEmail((String)value);
		if(!emailExists) {
			((UIInput) component).setValid(false);
			FacesMessage message = new FacesMessage("This e-mail has not been registered");
			context.addMessage(component.getClientId(context), message);
		}
	}
	
	public void resetPasswordRequest() throws IOException {
		Optional<User> userOptional = userRepository.findByEmail(email);;
		User existingUser = userOptional.orElse(null);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		if(existingUser != null) {
			PasswordResetToken passwordResetToken = createPasswordResetToken(existingUser);
			resetTokenRepository.save(passwordResetToken);

			EmailSender emailSender = new EmailSender(javaMailSender);
			
			emailSender.sendMail(passwordResetToken, 
					existingUser.getEmail(), 
					"Complete Password Reset", 
					"To complete the password reset process, please click here: "
							+ defaultDomain
							+ "user/reset-password?id=web&token=" 
							+ passwordResetToken.getToken() 
							+ " (This link will expire after 24 hours)");
			
			redirectMessage(facesContext);
			setEmail(null);
		} else {
			showErrorMessage(facesContext);
			setEmail(null);
		}
	}

	private void showErrorMessage(FacesContext facesContext) {
		FacesMessage facesMessage = new FacesMessage("There was a problem. Please try again.");
		facesContext.addMessage(null, facesMessage);
	}

	private void redirectMessage(FacesContext facesContext) throws IOException {
		facesContext.addMessage(null, new FacesMessage("Check your inbox at " + email + " to complete password reset."));
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
		facesContext.getExternalContext().redirect("/login");
	}
	
	
	private PasswordResetToken createPasswordResetToken(User existingUser) {
		String token = UUID.randomUUID().toString();
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setUser(existingUser);
		passwordResetToken.setToken(token);
		passwordResetToken.setExpiryDate();
		return passwordResetToken;
	}
	
}
