package com.trainingquizzes.english.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.json.JSONArray;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CreateSubjectsFromJsonFiles {
	
	public JsonArray searchSubjectJsonFiles(String path) throws IOException {
		File folder = new File(path);
		JsonArray jsonArray = new JsonArray();
		
		if(folder.exists()) {
			for (File fileEntry : folder.listFiles()) {
				
				InputStream is = new FileInputStream(fileEntry);
				
				 BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			     String jsonText = readAll(rd);
			     
			     JsonArray jsonArraySubject = JsonParser.parseString(jsonText).getAsJsonArray();;
			     
			     
			     jsonArray.add(jsonArraySubject);
			}
		}
		return jsonArray;
	}
	
	public JsonArray searchExercisesJsonFiles(String path, String fileName) throws IOException {
		File folder = new File(path);
		JsonArray jsonArray = new JsonArray();
		
		if(folder.exists()) {
			for (File fileEntry : folder.listFiles()) {
				
				InputStream is = new FileInputStream(fileEntry);
				
				if(fileEntry.getName().equals(fileName)) {
					BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				    String jsonText = readAll(rd);
				    JsonArray jsonArraySubject = JsonParser.parseString(jsonText).getAsJsonArray();;
				    jsonArray.add(jsonArraySubject);					
				}
			}
		}
		return jsonArray;
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }
	

}
