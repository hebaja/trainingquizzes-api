package com.trainingquizzes.english.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.trainingquizzes.english.model.FileChecksum;
import com.trainingquizzes.english.repository.FileChecksumRepository;

import static com.trainingquizzes.english.util.Constants.JSON_EXERCISES_FILES_PATH;
import static com.trainingquizzes.english.util.Constants.JSON_SUBJECTS_FILES_PATH;;

@Service
public class ChecksumWatcher {
	
	@Autowired
	private FileChecksumRepository repository;
	
	public void watchExerciseJsonFiles() throws FirebaseMessagingException {
		File folder = new File(JSON_EXERCISES_FILES_PATH);
		JSONArray jsonArray = new JSONArray();
		checkIfChecksumHasChanged(folder, jsonArray);
		sendMessage(jsonArray, "no messages for exercises checksum");
	}
	
	public void watchSubjectJsonFiles() throws FirebaseMessagingException {
		File folder = new File(JSON_SUBJECTS_FILES_PATH);
		JSONArray jsonArray = new JSONArray();
		checkIfChecksumHasChanged(folder, jsonArray);
		sendMessage(jsonArray, "no messages for subjects checksum");
	}

	private void sendMessage(JSONArray jsonArray, String noChangeMessage) throws FirebaseMessagingException {
		if(jsonArray.isEmpty()) {
			System.out.println(noChangeMessage);
		} else {
			Message message = Message.builder()
					.putData("files", jsonArray.toString())
					.setTopic("file_checksum")
					.build();
			
			String response = FirebaseMessaging.getInstance().send(message);
			System.out.println("Successfully sent message -> " + response);

		}
	}

	private void checkIfChecksumHasChanged(File folder, JSONArray jsonArray) {
		if(folder.exists()) {
			for (File fileEntry : folder.listFiles()) {
				try {
					InputStream fileInputStream = new FileInputStream(fileEntry);
					byte[] bytes = fileInputStream.readAllBytes();
					MessageDigest md = MessageDigest.getInstance("MD5");
					md.reset();
					md.update(bytes);
					byte[] digest = md.digest();
					BigInteger bigInt = new BigInteger(1, digest);
					String hashText = bigInt.toString(16);
					
					while(hashText.length() < 32) {
						hashText = "0"+hashText;
					}
															
					Optional<FileChecksum> fileChecksumOptional = repository.findById(fileEntry.getName());
					FileChecksum fileChecksum = fileChecksumOptional.orElse(null);
					if(fileChecksum != null) {
						String checksum = fileChecksum.getChecksum();
						
						if(!checksum.equals(hashText)) {
							System.out.println("checksum of " + fileEntry.getName() + " has changed.");
							FileChecksum newFileChecksum = new FileChecksum(fileEntry.getName(), hashText);
							repository.save(newFileChecksum);
							JSONObject file = new JSONObject();
							file.put("fileName", fileEntry.getName());
							jsonArray.put(file);
						}
					}
					
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("error message -----> " + e.getMessage());
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					System.out.println("error message -----> " + e.getMessage());
				}
			}
		}
	}
	
	public void addExerciseFileChecksumToDataBase() {
		List<FileChecksum> fileChecksumList = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();
//		File folder = new File(System.getProperty("user.home"), "webapps/ROOT/WEB-INF/classes/static/files/json/");
		
		File folder = new File(JSON_EXERCISES_FILES_PATH);
		
		generateAndSaveFileChecksum(fileChecksumList, stringBuilder, folder);
	}
	
	public void addSubjectsFileChecksumToDataBase() {
		List<FileChecksum> fileChecksumList = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();
		File folder = new File(JSON_SUBJECTS_FILES_PATH);
		generateAndSaveFileChecksum(fileChecksumList, stringBuilder, folder);
	}

	private void generateAndSaveFileChecksum(List<FileChecksum> fileChecksumList, StringBuilder stringBuilder,
			File folder) {
		int count = 0;
		
		if(folder.exists()) {
			stringBuilder.append("{");
			stringBuilder.append("\n");
			
			for (File fileEntry : folder.listFiles()) {
				try {
					InputStream fileInputStream = new FileInputStream(fileEntry);
					byte[] bytes = fileInputStream.readAllBytes();
			
					MessageDigest md = MessageDigest.getInstance("MD5");
					md.reset();
					md.update(bytes);
					byte[] digest = md.digest();
					BigInteger bigInt = new BigInteger(1, digest);
					String hashText = bigInt.toString(16);
					while(hashText.length() < 32) {
						hashText = "0"+hashText;
					}
					
					stringBuilder.append("\t");
					stringBuilder.append('"');
					stringBuilder.append(fileEntry.getName());
					stringBuilder.append('"');
					stringBuilder.append(":");
					stringBuilder.append('"');
					stringBuilder.append(hashText);
					stringBuilder.append('"');
					
					FileChecksum fileChecksum = new FileChecksum(fileEntry.getName(), hashText);
					fileChecksumList.add(fileChecksum);
					
					if(count < folder.listFiles().length - 1) {
						stringBuilder.append(",");
					}
					stringBuilder.append("\n");
					count++;
					fileInputStream.close();
					
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("error message -----> " + e.getMessage());
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					System.out.println("error message -----> " + e.getMessage());
				}
			}
			
			stringBuilder.append("}");
			repository.saveAll(fileChecksumList);
		} else {
			System.out.println("couldn't find folder");
		}
	}
}
