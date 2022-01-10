package com.trainingquizzes.english.bean;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.TaskRepository;
import com.trainingquizzes.english.repository.UserRepository;

@Controller
@ViewScoped
public class HomeBean {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	private String remoteUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
	
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
//							Task task = new Gson().fromJson(taskJsonObject, Task.class);
//							taskList.add(task);
//						}
//						
//					}
//					
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
//		subjects.forEach(subject -> {
//			subjectRepository.save(subject);
//			taskRepository.saveAll(subject.getTasks()); 
//		});
//		
//	}
}
