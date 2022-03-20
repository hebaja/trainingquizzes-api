package com.trainingquizzes.english;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

import de.dentrassi.crypto.pem.PemKeyStoreProvider;

@SpringBootApplication
@EnableCaching
public class TrainingQuizzesApplication extends SpringBootServletInitializer {
	
	@Value("${spring-english-training-quizzes-default-domain}")
	private String defaultDomain;
	
	public static void main(String[] args) {
		SpringApplication.run(TrainingQuizzesApplication.class, args);
		
		Security.addProvider(new PemKeyStoreProvider());
		try {
			KeyStore keystore = KeyStore.getInstance("PEM");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}

}
