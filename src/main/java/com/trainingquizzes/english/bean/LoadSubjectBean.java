package com.trainingquizzes.english.bean;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.trainingquizzes.english.dto.SubjectDto;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.TaskRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.util.SubjectBeanUtil;

@Controller
@ViewScoped
public class LoadSubjectBean {
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${spring-english-training-quizzes-firebase-message-topic}")
	private String firebaseMessageTopic;

	private SubjectBeanUtil subjectBeanUtil;
	
	private Subject subject;
	private Subject subjectToBeRemoved;
	private User user;
	private List<Subject> subjects;
	private List<Task> tasks;
	
	private long subjectId;
	
	private boolean subjectFormRender;
	private boolean subjectListRender = true;
	
	@PostConstruct
	private void init() {
		System.out.println("LoadSubjects -> " + firebaseMessageTopic);
		subjectBeanUtil = new SubjectBeanUtil(firebaseMessageTopic);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userInfo = authentication.getName();
		
		Optional<User> userOptional = userRepository.findByEmail(userInfo);
		user = userOptional.orElse(userRepository.findByUid(userInfo).orElse(null));
		
		if(user != null) {
			Optional<List<Subject>> subjectsOptional = subjectRepository.findAllByUser(user);
			subjects = subjectsOptional.orElse(null);
		}
	}
	
	public void openSubject(Subject subject) {
		this.subject = subject;
		if(subject != null) {
			setSubjectFormRender(true);
			setSubjectListRender(false);
		} else {
			addMessage(FacesMessage.SEVERITY_ERROR, "English Training Quizzes", "Subject not found.");
		}
	}
	
	public void returnToSubjects() {
		setSubjectFormRender(false);
		setSubjectListRender(true);
	}
	
	@CacheEvict(value = {"subjectsList", "userWithSubjectsList", "subject"}, allEntries = true)
	public void updateSubject() {
		subjectBeanUtil.saveSubject(subjectRepository, taskRepository, subject, subject.getUser(), "Subject successfully updated.");
	}
	
	public void fetchSubjectToBeRemoved(Subject subjectToBeRemoved) {
		this.subjectToBeRemoved = subjectToBeRemoved;
	}
	
	@CacheEvict(value = {"subjectsList", "userWithSubjectsList"}, allEntries = true)
	public void removeSubject() {
		subjectRepository.delete(subjectToBeRemoved);
		
		Message message = prepareFirebaseMessage();
		sendFirebaseMessage(message);
		
		Optional<List<Subject>> subjectsOptional = subjectRepository.findAllByUser(user);
		subjects = subjectsOptional.orElse(null);
	}

	private void sendFirebaseMessage(Message message) {
		try {
			String response = FirebaseMessaging.getInstance().send(message);
			System.out.println("Message successfully sent: " + response);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
	}

	private Message prepareFirebaseMessage() {
		
		SubjectDto subjectDto = SubjectDto.convertFromSubject(subjectToBeRemoved);
		ObjectMapper objectMapper = new ObjectMapper();
		String subjectDtoJsonString = null;
		
		try {
			subjectDtoJsonString = objectMapper.writeValueAsString(subjectDto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		Message message = Message.builder()
				.putData("subject_removed", subjectDtoJsonString)
				.setTopic("file_checksum")
				.build();
		return message;
	}
	
	public void addOption(int index) {
		subjectBeanUtil.addOption(subject, index);
	}
	
	public void removeOption(int index) {
		subjectBeanUtil.removeOption(subject, index);
	}
	
	public void addTask() {
		subjectBeanUtil.addTask(subject);
	}
	
	public void removeTask() {
		subjectBeanUtil.removeTask(subject);
	}
	
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }
	
	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
	
	public boolean getSubjectFormRender() {
		return subjectFormRender;
	}

	public void setSubjectFormRender(boolean subjectFormRender) {
		this.subjectFormRender = subjectFormRender;
	}

	public boolean isSubjectListRender() {
		return subjectListRender;
	}

	public void setSubjectListRender(boolean subjectListRender) {
		this.subjectListRender = subjectListRender;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
}
