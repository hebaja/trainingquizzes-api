package com.trainingquizzes.english.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class TaskOption {

    private String prompt;
    private boolean correct;
    
    public TaskOption() {}

    public TaskOption(String prompt, boolean correct) {
        this.prompt = prompt;
        this.setCorrect(correct);
    }

    public String getPrompt() { return prompt; }

    public void setPrompt(String option0) {
        this.prompt = option0;
    }

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	    
}
