package com.trainingquizzes.english.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfigurer implements WebMvcConfigurer{
	
	@Value("${spring-english-training-quizzes-default-domain}")
	private String[] defaultDomainOrigin;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins(defaultDomainOrigin).allowedMethods("GET", "POST", "PUT", "DELETE");
	}

}
