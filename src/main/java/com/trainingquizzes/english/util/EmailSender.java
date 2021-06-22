package com.trainingquizzes.english.util;

import static com.trainingquizzes.english.util.Constants.EMAIL_SET;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.trainingquizzes.english.token.Token;

public class EmailSender {
	
	private final JavaMailSender javaMailSender;
	
	public EmailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void sendMail(Token userToken, String emailAddress, String subject, String text) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(emailAddress);
		mailMessage.setSubject(subject);
		mailMessage.setFrom(EMAIL_SET);
		mailMessage.setText(text);
		
		javaMailSender.send(mailMessage);
	}

}
