package com.trainingquizzes.english;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class EnglishJsfFacesApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(EnglishJsfFacesApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(EnglishJsfFacesApplication.class, args);
	}

}
