package com.trainingquizzes.english.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleAuthenticationUtil {
	
	@Value("{spring.security.oauth2.client.registration.google.client-id}")
	private static String googleClientId;
	
	public static GoogleIdTokenVerifier retrieveIdTokenVerifier() {
		HttpTransport transport = null;
		try {
			transport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		final JacksonFactory jacksonFactory = new JacksonFactory();
		return new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
				.setAudience(Collections.singletonList(googleClientId))
				.build();
	}

}
