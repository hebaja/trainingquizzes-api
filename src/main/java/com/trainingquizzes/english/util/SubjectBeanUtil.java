package com.trainingquizzes.english.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.trainingquizzes.english.dto.SubjectDto;
import com.trainingquizzes.english.dto.UserWithSubjectDto;
import com.trainingquizzes.english.dto.UserWithSubjectsDto;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.model.TaskOption;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.TaskRepository;

public class SubjectBeanUtil {
	
	private List<Task> tasks = new ArrayList<Task>();
	private boolean tasksPromptsAreValid = true;
	private boolean tasksOptionsAreValid = true;
	
	private String firebaseMessageTopic;
	
	public SubjectBeanUtil(String firebaseMessageTopic) {
		this.firebaseMessageTopic = firebaseMessageTopic;
	}
	
	public void addTask(Subject subject) {
		Task task = new Task();
		List<TaskOption> options = new ArrayList<TaskOption>(Arrays.asList(new TaskOption(), new TaskOption()));
		task.setOptions(options);
		task.setSubject(subject);
		subject.getTasks().add(task);		
	}
	
	public void removeTask(Subject subject) {
		if(subject.getTasks().size() <= 1) {
			addMessage(FacesMessage.SEVERITY_ERROR, "English Training Quizzes", "Subject must have at least 1 task.");
		} else {
			subject.getTasks().remove(subject.getTasks().size() -1);
		}
	}
	
	public void addOption(Subject subject, int index) {
		if(subject.getTasks().get(index).getOptions().size() <= 4) {
			subject.getTasks().get(index).getOptions().add(new TaskOption());
		} else {
			addMessage(FacesMessage.SEVERITY_ERROR, "English Training Quizzes", "Options limit exceeded.");
		}
	}
	
	public void removeOption(Subject subject, int index) {
		if(subject.getTasks().get(index).getOptions().size() <= 2) {
			addMessage(FacesMessage.SEVERITY_ERROR, "English Training Quizzes", "Task needs 2 options at least.");
		} else {
			subject.getTasks().get(index).getOptions().remove(subject.getTasks().get(index).getOptions().size() - 1);
		}
	}
	
	public void saveSubject(SubjectRepository subjectRepository, TaskRepository taskRepository, Subject subject, User user, String successMessage) {
		validateInputs(subject.getTasks());
		
		if(tasksPromptsAreValid && tasksOptionsAreValid) {
			subject.setUser(user);
			Subject subjectSaved = subjectRepository.save(subject);;
			taskRepository.saveAll(subject.getTasks());

			System.out.println("message_topic " + firebaseMessageTopic);
			
			Message message = prepareFirebaseMessage(user, subjectSaved);
			sendFirebaseMessage(message);
			
			addMessage(FacesMessage.SEVERITY_INFO, "English Training Quizzes", successMessage);
		} else {
			addMessage(FacesMessage.SEVERITY_ERROR, "English Training Quizzes", "Don't leave empty inputs.");
		}
	}

	private void sendFirebaseMessage(Message message) {
		try {
			String response = FirebaseMessaging.getInstance().send(message);
			System.out.println("Message successfully sent: " + response + " with topic " + firebaseMessageTopic);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
	}

	private Message prepareFirebaseMessage(User user, Subject subjectSaved) {
		UserWithSubjectDto userWithSubjectDto = UserWithSubjectDto.convert(user, subjectSaved);;
		ObjectMapper objectMapper = new ObjectMapper();
		
		String subjectDtoJsonString = null;
		try {
			subjectDtoJsonString = objectMapper.writeValueAsString(userWithSubjectDto);
		} catch (JsonProcessingException e) {
			System.out.println("There was a problem trying to parse Subject to json: " + e.getMessage());
			addMessage(FacesMessage.SEVERITY_ERROR, "English Training Quizzes", "There was a problem when trying to save subject.");
			e.printStackTrace();
		}
		
		Message message = Message.builder()
				.putData("user_with_subject", subjectDtoJsonString)
				.setTopic(firebaseMessageTopic)
				.build();
		return message;
	}
	
	public Subject searchSubject(SubjectRepository subjectRepository, long subjectId) {
		System.out.println("searching");
		
		Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
		Subject subjectLoaded = subjectOptional.orElse(null);
		
		System.out.println(subjectLoaded);
		
		subjectLoaded.setTasks(tasks);
		return subjectLoaded;
	}
	private void validateInputs(List<Task> tasks) {
		tasks.forEach(task -> {
			if(task.getPrompt().isEmpty()) {
				tasksPromptsAreValid = false;
			} else {
				tasksPromptsAreValid = true;
			}
			task.getOptions().forEach(option -> {
				if(option.getPrompt().isEmpty()) {
					tasksOptionsAreValid = false;
				} else {
					tasksOptionsAreValid = true;
				}
			});
		});
	}
	
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }


}
