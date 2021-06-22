package com.trainingquizzes.english.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class Subject {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String title;
    
//    @ManyToMany(cascade = javax.persistence.CascadeType.REMOVE, fetch = FetchType.EAGER)
//	@OneToMany(cascade = CascadeType.REMOVE)
//	@JoinColumn(name = "subject_id")
//	private List<Task> tasks;
	
	@ManyToOne
	private User user;
	
	@OneToMany(mappedBy = "subject")
    @Cascade(CascadeType.DELETE)
	private List<Task> tasks;
	
	public Subject() {}
	
	public Subject(String title, List<Task> tasks) {
		this.title = title;
		this.tasks = tasks;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
}
