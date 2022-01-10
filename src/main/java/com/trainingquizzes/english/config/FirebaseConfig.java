package com.trainingquizzes.english.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

public class FirebaseConfig {
	
	private String path;
	
	@Value("${spring-english-training-quizzes-firebase-message-topic}")
	private String firebaseMessageTopic;
	
	
	public FirebaseConfig(String path) {
		this.path = path;
	}

	public void configure() throws IOException {
		FileInputStream credentials = new FileInputStream(path);
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(credentials);
		FirebaseOptions options = FirebaseOptions
				.builder()
				.setCredentials(googleCredentials)
				.build();
		
		System.out.println("starting firebase");
		
		FirebaseApp.initializeApp(options);
	}
	
	public void sendTestMessage() throws FirebaseMessagingException {
		
		List<String> files = new ArrayList<>(Arrays.asList("file1", "file2", "file3"));
		String[] myStringArray = {"file1", "file2", "file3"};
		
		
		System.out.println(files.toString());
		System.out.println("array --> " + myStringArray);
		
		JSONObject file1 = new JSONObject();
		JSONObject file2 = new JSONObject();
		JSONObject file3 = new JSONObject();
		
		file1.put("file", "file1");
		file2.put("file", "file2");
		file3.put("file", "file3");
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(file1);
		jsonArray.put(file2);
		jsonArray.put(file3);
		
		String finalMessage = jsonArray.toString();
		
		Message message = Message.builder()
				.putData("files", finalMessage)
				.setTopic(firebaseMessageTopic)
				.build();
		
		String response = FirebaseMessaging.getInstance().send(message);
		System.out.println("Successfully sent message " + response + " with topic " + firebaseMessageTopic);
	}
}