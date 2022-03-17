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
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Task {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String prompt;
	
	@ManyToOne
	private Subject subject;
	
    @ElementCollection(fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<TaskOption> options;
    
    @Transient
    private int rightOption;
    
    private boolean shuffleOptions;
    
    public long getId() {
		return id;
	}

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

	public List<TaskOption> getOptions() {
        return options;
    }

    public void setOptions(List<TaskOption> options) {
        this.options = options;
    }

	public boolean isShuffleOptions() {
		return shuffleOptions;
	}

	public void setShuffleOptions(boolean shuffleOptions) {
		this.shuffleOptions = shuffleOptions;
	}
    
    public int getRightOption() {
		return rightOption;
	}

	public void setRightOption(int rightOption) {
		this.rightOption = rightOption;
	}
	
}
