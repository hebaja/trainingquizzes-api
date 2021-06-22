package com.trainingquizzes.english.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FileChecksum {
	
	@Id
	private String fileName;
	private String checksum;
	
	public FileChecksum() {}
	
	public FileChecksum(String fileName, String checksum) {
		this.fileName = fileName;
		this.checksum = checksum;
	}

	public String getFileName() {
		return fileName;
	}

	public String getChecksum() {
		return checksum;
	}
	
	
	

}
