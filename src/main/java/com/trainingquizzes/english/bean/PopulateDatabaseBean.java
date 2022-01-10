//package com.trainingquizzes.english.bean;
//
//import static com.trainingquizzes.english.util.Constants.JSON_EXERCISES_FILES_PATH;
//import static com.trainingquizzes.english.util.Constants.JSON_SUBJECTS_FILES_PATH;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import javax.faces.context.FacesContext;
//import javax.faces.view.ViewScoped;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.trainingquizzes.english.enums.LevelType;
//import com.trainingquizzes.english.model.Subject;
//import com.trainingquizzes.english.model.SubjectOld;
//import com.trainingquizzes.english.model.Task;
//import com.trainingquizzes.english.model.TaskOld;
//import com.trainingquizzes.english.model.TaskOption;
//import com.trainingquizzes.english.model.User;
//import com.trainingquizzes.english.repository.SubjectRepository;
//import com.trainingquizzes.english.repository.TaskRepository;
//import com.trainingquizzes.english.repository.UserRepository;
//import com.trainingquizzes.english.util.CreateSubjectsFromJsonFiles;
//
//@Controller
//@ViewScoped
//public class PopulateDatabaseBean {
//	
//	@Autowired
//	private UserRepository userRepository;
//	
//	@Autowired
//	private SubjectRepository subjectRepository;
//	
//	@Autowired
//	private TaskRepository taskRepository;
//	
//	public void populate() {
//		CreateSubjectsFromJsonFiles subjectsUtil = new CreateSubjectsFromJsonFiles();
//		
//		List<Subject> subjects = new ArrayList<Subject>();
//		
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String userInfo = authentication.getName();
//		
//		Optional<User> userOptional = userRepository.findByEmail(userInfo);
//		User user = userOptional.orElse(userRepository.findByUid(userInfo).orElse(null));
//		
//		try {
//			JsonArray jsonArraySubjects = subjectsUtil.searchSubjectJsonFiles(JSON_SUBJECTS_FILES_PATH);
//			
//			for (JsonElement jsonElementArray : jsonArraySubjects) {
//				JsonArray jArray = jsonElementArray.getAsJsonArray();
//				for(JsonElement jElement : jArray) {
//					JsonObject jsonObject = jElement.getAsJsonObject();
//					SubjectOld subjectOld = new Gson().fromJson(jsonObject, SubjectOld.class);
//					
//					List<Task> taskList = new ArrayList<Task>();
//				
//					JsonArray tasksArray = subjectsUtil.searchExercisesJsonFiles(JSON_EXERCISES_FILES_PATH, subjectOld.getFileName());
//					for(JsonElement jTaskElement : tasksArray) {
//						JsonArray tJsonArray = jTaskElement.getAsJsonArray();
//						for(JsonElement tJsonElement : tJsonArray) {
//							JsonObject taskJsonObject = tJsonElement.getAsJsonObject();
//							TaskOld taskOld = new Gson().fromJson(taskJsonObject, TaskOld.class);
//							
//							Task task = new Task();
//							task.setPrompt(taskOld.getPrompt());
//							
//							List<TaskOption> taskOptionList = new ArrayList<TaskOption>();
//							
//							taskOld.getOptions().forEach(option -> {
//								TaskOption taskOption = new TaskOption();
//								taskOption.setPrompt(option.getPrompt());
//								taskOptionList.add(taskOption);
//							});
//							
//							
//							if(taskOld.getRightOption() == 0) {
//								taskOptionList.get(0).setCorrect(true);
//							} else if(taskOld.getRightOption() == 1) {
//								taskOptionList.get(1).setCorrect(true);
//							} else if(taskOld.getRightOption() == 2) {
//								taskOptionList.get(2).setCorrect(true);
//							} else if(taskOld.getRightOption() == 3) {
//								taskOptionList.get(3).setCorrect(true);
//							} else if(taskOld.getRightOption() == 4) {
//								taskOptionList.get(4).setCorrect(true);
//							}
//							
//							task.setOptions(taskOptionList);
//							taskList.add(task);
//						}
//						
//					}
//					Subject subject = new Subject();
//					subject.setTitle(subjectOld.getPrompt());
//					subject.setUser(user);
//					subject.setTasks(taskList);
//					
//					String fileName = subjectOld.getFileName();
//					
//					if(fileName.substring(0, 4).equals("easy")) {
//						subject.setLevel(LevelType.EASY);
//					}
//					if(fileName.substring(0, 4).equals("hard")) {
//						subject.setLevel(LevelType.HARD);
//					}
//					if(fileName.substring(0, 4).equals("medi")) {
//						subject.setLevel(LevelType.MEDIUM);
//					}
//					
//					subjects.add(subject);
//				}
//            }
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//		subjects.forEach(subject -> {
//			subjectRepository.save(subject);
////			taskRepository.saveAll(subject.getTasks());
//			subject.getTasks().forEach(task -> {
//				task.setSubject(subject);
//				taskRepository.save(task);
//			}); 
//		});
//		
//	}
//
//}
