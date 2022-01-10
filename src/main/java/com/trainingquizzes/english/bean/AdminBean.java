package com.trainingquizzes.english.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.trainingquizzes.english.config.FirebaseConfig;
import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.TaskRepository;
import com.trainingquizzes.english.repository.UserRepository;

@Controller
@ViewScoped
public class AdminBean {
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${spring-english-training-quizzes-firebase-config-path}")
	private String firebaseConfigPath;
	
	@Value("${spring-english-training-quizzes-firebase-message-topic}")
	private String firebaseMessageTopic;
	
	public void startFirebase(){
		FirebaseConfig firebase = new FirebaseConfig(firebaseConfigPath);
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

	public void sendMessage() {
		FirebaseConfig firebase = new FirebaseConfig(firebaseConfigPath);
		
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
	
//	private void searchThroughSubjectsFolder() throws IOException {
//		File folder = new File(JSON_SUBJECTS_FILES_PATH);
//		
//		List<Subject> subjectList = new ArrayList<Subject>();
//		
//		List<SubjectOld> subjectOldList = new ArrayList<SubjectOld>();
//		
//		if(folder.exists()) {
//			for (File fileEntry : folder.listFiles()) {
//				
//				JsonArray subjectsJsonArray = JsonReader.readSubjectsJsonFromUrl(fileEntry.getName());
//				
//				if(subjectsJsonArray != null) {
//					for(JsonElement jsonElement : subjectsJsonArray) {
//						JsonObject jsonObject = jsonElement.getAsJsonObject();
//						final SubjectOld subject = new Gson().fromJson(jsonObject, SubjectOld.class);
//						SubjectOld subjectOld = new Gson().fromJson(jsonObject, SubjectOld.class);
//						subjectOldList.add(subjectOld);
//					}
//				}
//			}
//		}
		
//		subjectOldList.forEach(subjectOld -> {
//			
//			if(subjectOld.getFileName().substring(0, 4).equals("easy")) {
//				buildNewSubjectList(subjectList, subjectOld, LevelType.EASY);
//			}
//			
//			if(subjectOld.getFileName().substring(0, 4).equals("medi")) {
//				buildNewSubjectList(subjectList, subjectOld, LevelType.MEDIUM);
//			}
//			
//			if(subjectOld.getFileName().substring(0, 4).equals("hard")) {
//				buildNewSubjectList(subjectList, subjectOld, LevelType.HARD);
//			}
//		});
		
//		subjectList.forEach(subject -> {
//			System.out.println("*****************************************************************************************");
//			System.out.println("########## title -> " + subject.getTitle() + " level -> " + subject.getLevel() + " ##########");
//			subject.getTasks().forEach(task -> {
//				System.out.println("task -> " + task.getPrompt());
//				task.getOptions().forEach(option -> {
//					System.out.println("option -> " + option.getPrompt() + " - isCorrect -> " + option.isCorrect());
//				});
//			});
//		});
		
//		subjectList.forEach(subject -> {
//			if(subject.getLevel().equals(LevelType.HARD)) {
//				System.out.println(subject.getTitle() + " - " + subject.getLevel());
//			}
//		});
		
//		subjectList.forEach(subject -> {
//			subjectRepository.save(subject);
//			taskRepository.saveAll(subject.getTasks());
//			System.out.println(subject.getTitle() + " saved");
//			
//			
//		});
//	}
	
//	private void buildNewSubjectList(List<Subject> subjectList, SubjectOld subjectOld, LevelType level) {
//		List<Task> taskList = new ArrayList<Task>();
//		buildTaskListFromJson(subjectOld, taskList);
//		User user = retrieveUser();
//		Subject subject = new Subject(subjectOld.getPrompt(), taskList, user, level);
//		configureTaskList(taskList, subject);
//		subjectList.add(subject);
//	}

	private void configureTaskList(List<Task> taskList, Subject subject) {
		taskList.forEach(task -> {
			task.setSubject(subject);
			if(task.getRightOption() == 0) {
				task.getOptions().get(0).setCorrect(true);
			}
			if(task.getRightOption() == 1) {
				task.getOptions().get(1).setCorrect(true);
			}
			if(task.getRightOption() == 2) {
				task.getOptions().get(2).setCorrect(true);
			}
			if(task.getRightOption() == 3) {
				task.getOptions().get(3).setCorrect(true);
			}
			if(task.getRightOption() == 4) {
				task.getOptions().get(4).setCorrect(true);
			}
			
		});
	}

//	private void buildTaskListFromJson(SubjectOld subjectOld, List<Task> taskList) {
//		try {
//			JsonArray jsonExerciseArray = JsonReader.readExerciseJsonFromUrl(subjectOld.getFileName());
//			if (jsonExerciseArray != null) {
//		        for (JsonElement jsonElement : jsonExerciseArray) {
//		            JsonObject jsonObject = jsonElement.getAsJsonObject();
//		            final Task task = new Gson().fromJson(jsonObject, Task.class);
//		            taskList.add(task);
//		        }
//		    }
//			
//		} catch (EjbAccessException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
//	private void saveASingleExerciseFromJsonFile(String fileName, User user) throws IOException {
//		JsonArray jsonExerciseArray = JsonReader.readExerciseJsonFromUrl(fileName);
//		
//		List<Task> taskList = generateTaskList(jsonExerciseArray);
//	
//		saveSubject(fileName, user, taskList);
//		
//	}

	private List<Task> generateTaskList(JsonArray jsonExerciseArray) {
		List<Task> taskList = new ArrayList<Task>();
		
		if (jsonExerciseArray != null) {
            for (JsonElement jsonElement : jsonExerciseArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                final Task task = new Gson().fromJson(jsonObject, Task.class);
                taskList.add(task);
            }
        }
		return taskList;
	}

	private void saveSubject(String fileName, User user, List<Task> fullTasksList) {
		Subject subject = new Subject("Comparative and superlative", fullTasksList, user, LevelType.EASY);
		configureTaskList(fullTasksList, subject);
		subjectRepository.save(subject);
		taskRepository.saveAll(subject.getTasks());
	}

//	private void searchThroughExercisesFolder() throws IOException {
//		File folder = new File(JSON_EXERCISES_FILES_PATH);
//		
//		if(folder.exists()) {
//			for (File fileEntry : folder.listFiles()) {
//				System.out.println(fileEntry.getName());
//				JsonArray jsonExerciseArray = JsonReader.readExerciseJsonFromUrl(fileEntry.getName());
//				
//				List<Task> fullTasksList = new ArrayList<Task>();
//				
//				if (jsonExerciseArray != null) {
//		            for (JsonElement jsonElement : jsonExerciseArray) {
//		                JsonObject jsonObject = jsonElement.getAsJsonObject();
//		                final Task task = new Gson().fromJson(jsonObject, Task.class);
//		                fullTasksList.add(task);
//		            }
//		        }
//				
//				System.out.println("size -> " + fullTasksList.size());
//			}
//		}
//	}
	
	private User retrieveUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userInfo = authentication.getName();
		Optional<User> userOptional = userRepository.findByEmail(userInfo);
		return userOptional.orElse(userRepository.findByUid(userInfo).orElse(null));
	}

	public String getPath() {
		return firebaseConfigPath;
	}

	public void setPath(String path) {
		this.firebaseConfigPath = path;
	}
			
}
