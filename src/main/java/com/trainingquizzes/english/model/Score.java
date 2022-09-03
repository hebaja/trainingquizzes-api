package com.trainingquizzes.english.model;

public class Score {

	private String username;
	private String pictureUrl;
	private double score;
	
	public Score(String username, String pictureUrl, double score) {
		this.username = username;
		this.pictureUrl = pictureUrl;
		this.score = score;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public double getScore() {
		return score;
	}

}
