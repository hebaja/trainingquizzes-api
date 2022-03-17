package com.trainingquizzes.english.form;

import java.util.List;

public class TaskForm {

	private Long id;
	private String prompt;
	private boolean shuffleOptions;
	private List<OptionForm> options;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public boolean isShuffleOptions() {
		return shuffleOptions;
	}

	public void setShuffleOptions(boolean isShuffleOptions) {
		this.shuffleOptions = isShuffleOptions;
	}

	public List<OptionForm> getOptions() {
		return options;
	}

	public void setOptions(List<OptionForm> options) {
		this.options = options;
	}

}
