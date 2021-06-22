package com.trainingquizzes.english.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

@Embeddable
public class TaskOption {

    private String prompt;
    private boolean isCorrect;
    
    public TaskOption() {}

    public TaskOption(String prompt, boolean isCorrect) {
        this.prompt = prompt;
        this.isCorrect = isCorrect;
    }

    public String getPrompt() { return prompt; }

    public void setPrompt(String option0) {
        this.prompt = option0;
    }

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
    
}
