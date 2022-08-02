package com.trainingquizzes.english.form;

public class SaveTemporaryTrialDataStore {

	private Long id;
	private int tasksIndex;
	private boolean finished;
	private boolean correct; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTasksIndex() {
		return tasksIndex;
	}

	public void setTasksIndex(int tasksIndex) {
		this.tasksIndex = tasksIndex;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

}
