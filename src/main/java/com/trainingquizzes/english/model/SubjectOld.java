package com.trainingquizzes.english.model;

public class SubjectOld implements Comparable<SubjectOld> {
	
	private String prompt;
	private String fileName;
	private int counterOrder;
	
	public SubjectOld() {}

	public SubjectOld(String prompt, String fileName, int counterOrder) {
		this.prompt = prompt;
		this.fileName = fileName;
		this.counterOrder = counterOrder;
	}

	public SubjectOld(String prompt, String fileName) {
		this.prompt = prompt;
		this.fileName = fileName;
	}
	
	public String getPrompt() {
		return prompt;
	}

	public String getFileName() {
		return fileName;
	}

	public int getCounterOrder() {
		return counterOrder;
	}

	@Override
	public int compareTo(SubjectOld otherSubject) {
		return this.prompt.compareTo(otherSubject.prompt);
	}

}
