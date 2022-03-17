package com.trainingquizzes.english;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableCaching
public class TrainingQuizzesApplication extends SpringBootServletInitializer {
	
	@Value("${spring-english-training-quizzes-default-domain}")
	private String defaultDomain;
	
	public static void main(String[] args) {
		SpringApplication.run(TrainingQuizzesApplication.class, args);
	}

}
