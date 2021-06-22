package com.trainingquizzes.english.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class Task {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String prompt;
	
	@ManyToOne
	private Subject subject;
	
	@ElementCollection(fetch = FetchType.LAZY)
    @Cascade(CascadeType.DELETE)
    private List<Tag> tags;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Cascade(CascadeType.DELETE)
    private List<TaskOption> options;
    
    @NotNull
    public String getPrompt() {
            return prompt;
        }

    public void setPrompt(String prompt) {
            this.prompt = prompt;
        }
    
    public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<TaskOption> getOptions() {
        return options;
    }

    public void setOptions(List<TaskOption> options) {
        this.options = options;
    }
    
    @Embeddable
    public static class Tag {
    	
    	private String title;
    	
    	public Tag(String title) {
    		this.title = title;
    	}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
    	
    }
}
