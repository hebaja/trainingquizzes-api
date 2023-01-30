package com.trainingquizzes.english.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseConfig {
	
	Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

	public void configure() throws IOException {
		FileInputStream credentials = new FileInputStream(System.getenv("ENGLISH_TRAINING_QUIZZES_FIREBASE_CONFIG_PATH"));
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(credentials);
		FirebaseOptions options = FirebaseOptions
				.builder()
				.setCredentials(googleCredentials)
				.build();
		
		if(!firebaseAppHasBeenInitialized()) {
			logger.info("Starting Firebase app using name: {}", FirebaseApp.DEFAULT_APP_NAME);
			FirebaseApp.initializeApp(options);
		} else {
			logger.warn("Firebase app with name {} is already running", FirebaseApp.DEFAULT_APP_NAME);
		}
	}

	private boolean firebaseAppHasBeenInitialized() {
		boolean hasBeenInitialized = false;
		List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
		for(FirebaseApp app : firebaseApps){
		    if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)){
		        hasBeenInitialized = true;
		    }
		}
		
		return hasBeenInitialized;
	}

}