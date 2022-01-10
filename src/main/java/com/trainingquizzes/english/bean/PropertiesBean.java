package com.trainingquizzes.english.bean;

import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
@ViewScoped
public class PropertiesBean {

	@Value("${spring-application-name}")
	private String applicationName;
	
	@Value("${ENGLISH_TRAINING_QUIZZES_MESSAGE}")
	private String message;
	
	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
