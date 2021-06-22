package com.trainingquizzes.english.bean;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.trainingquizzes.english.config.FirebaseConfig;
import com.trainingquizzes.english.util.ChecksumWatcher;

@Controller
@ViewScoped
public class AdminBean {
	
	@Autowired
	private ChecksumWatcher checksumWatcher;
	
	public void startFirebase(){
		FirebaseConfig firebase = new FirebaseConfig();
		try {
			firebase.configure();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage("firebase started");
			facesContext.addMessage(null, facesMessage);
		} catch (IOException e) {
			e.printStackTrace();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage("There was a problem trying to start Firebase.");
			facesContext.addMessage(null, facesMessage);
		}
	}

	public void buildFileChecksum() {
		try {
			checksumWatcher.addExerciseFileChecksumToDataBase();
			checksumWatcher.addSubjectsFileChecksumToDataBase();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage("Checksums generated");
			facesContext.addMessage(null, facesMessage);
		} catch (Exception e) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage("There was a problem trying to generate files");
			facesContext.addMessage(null, facesMessage);
		}
	}
	
	public void fileWatcher() {
		try {
			checksumWatcher.watchExerciseJsonFiles();
			checksumWatcher.watchSubjectJsonFiles();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage("Json files updated");
			facesContext.addMessage(null, facesMessage);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage("There was a problem and files weren't updated");
			facesContext.addMessage(null, facesMessage);
		}
	}
	
	public void sendMessage() {
		FirebaseConfig firebase = new FirebaseConfig();
		
		try {
			firebase.sendTestMessage();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage("Test messages sent");
			facesContext.addMessage(null, facesMessage);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage("Test messages couldn't be sent");
			facesContext.addMessage(null, facesMessage);
		}
	}
			
}
