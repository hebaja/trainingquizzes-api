//package com.trainingquizzes.english.util;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.nio.charset.Charset;
//
//import org.springframework.ejb.access.EjbAccessException;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonParser;
//
//import static com.trainingquizzes.english.util.Constants.JSON_EXERCISES_FILES_PATH;
//import static com.trainingquizzes.english.util.Constants.JSON_SUBJECTS_FILES_PATH;;
//
//public class JsonReader {
//	
//	private static String readAll(Reader rd) throws IOException {
//	    StringBuilder sb = new StringBuilder();
//	    int cp;
//	    while ((cp = rd.read()) != -1) {
//	      sb.append((char) cp);
//	    }
//	    return sb.toString();
//	  }
//	
//	public static JsonArray readExerciseJsonFromUrl(String fileName) throws IOException, EjbAccessException {
//		InputStream is = new FileInputStream(new File(JSON_EXERCISES_FILES_PATH + fileName));
//		return parseJsonFile(is);
//	}
//	
//	public static JsonArray readSubjectsJsonFromUrl(String fileName) throws IOException, EjbAccessException {
//		InputStream is = new FileInputStream(new File(JSON_SUBJECTS_FILES_PATH + fileName));
//		return parseJsonFile(is);
//	}
//
//	private static JsonArray parseJsonFile(InputStream is) throws IOException {
//		try {
//	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
//	      String jsonText = readAll(rd);
//	      
//	      return JsonParser.parseString(jsonText).getAsJsonArray();
//	    } finally {
//	      is.close();
//	    }
//	}
//}
