package com.trainingquizzes.english.bean;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.TaskOption;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.TaskRepository;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.util.SubjectBeanUtil;

@Controller
@ViewScoped
public class SubjectBean {
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${spring-english-training-quizzes-firebase-message-topic}")
	private String firebaseMessageTopic;
	
	private Subject subject = new Subject();
	private List<Task> tasks = new ArrayList<Task>();
	private SubjectBeanUtil subjectBeanUtil = new SubjectBeanUtil(firebaseMessageTopic);
	
	@PostConstruct
	private void init() {
		System.out.println("Subject bean -> " + firebaseMessageTopic);
		subjectBeanUtil = new SubjectBeanUtil(firebaseMessageTopic);
		addTask();
	}
	
	@CacheEvict(value = {"subjectsList", "userWithSubjectsList"}, allEntries = true)
	public void saveSubject() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userInfo = authentication.getName();
		
		Optional<User> userOptional = userRepository.findByEmail(userInfo);
		User user = userOptional.orElse(userRepository.findByUid(userInfo).orElse(null));
		
		try {
			subjectBeanUtil.saveSubject(subjectRepository, taskRepository, subject, user, "Subject successfully saved.");
			subject = new Subject();
			tasks = new ArrayList<Task>();
			addTask();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			addMessage(FacesMessage.SEVERITY_ERROR, "English Training Quizzes", "Subject could not be saved.");
		}
		
	}
	
	public void addTask() {
		subject.setTasks(tasks);
		subjectBeanUtil.addTask(subject);
	}
	
	public void removeTask() {
		subjectBeanUtil.removeTask(subject);
	}
	
	public void addOption(int index) {
		subjectBeanUtil.addOption(subject, index);
	}
	
	public void removeOption(int index) {
		subjectBeanUtil.removeOption(subject, index);
	}
	
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
}
