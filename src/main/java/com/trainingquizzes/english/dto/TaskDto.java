package com.trainingquizzes.english.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.model.TaskOption;

public class TaskDto {
	
	private Long id;
	private String prompt;
	private boolean shuffleOptions;
	private List<TaskOption> options;
	
	public TaskDto(Task task) {
		this.id = task.getId();
		this.prompt = task.getPrompt();
		this.shuffleOptions = task.isShuffleOptions();
		this.options = task.getOptions();
	}

	public Long getId() {
		return id;
	}

	public String getPrompt() {
		return prompt;
	}

	public boolean isShuffleOptions() {
		return shuffleOptions;
	}
	
	public List<TaskOption> getOptions() {
		return options;
	}

	public static List<TaskDto> convertList(List<Task> tasks) {
		return tasks.stream().map(TaskDto::new).collect(Collectors.toList());
	}
	
}
