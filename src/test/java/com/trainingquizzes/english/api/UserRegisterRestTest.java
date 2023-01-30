package com.trainingquizzes.english.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class UserRegisterRestTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldRequestUserRegisterAndReturn200() throws Exception {
		URI uri = new URI("/api/user-register");
		String json = buildJson("test@test.com");
		postRequest(uri, json, 200);
	}
	
	@Test
	void shouldReturn409CaseEmailAlreadyExistsInDB() throws Exception {
		URI uri = new URI("/api/user-register");
		String json = buildJson("student@hebaja.com");
		postRequest(uri, json, 409);
	}

	@Test
	void shouldConfirmUserRegisterAndReturn200() throws Exception {
		URI uri = new URI("/api/user-register/confirm");
		String json = "{"
				+ "\"token\":\"114858fc-70b9-497d-b0d7-dccfd7981e8d\"" 
				+ "}";
		postRequest(uri, json, 200);
	}
	
	@Test
	void shouldReturn400CaseUserRegisterTokenIsInvalid() throws Exception {
		URI uri = new URI("/api/user-register/confirm");
		String json = "{"
				+ "\"token\":\"114858fc-70b9-497d-xxxx-dccfd7981e8d\"" 
				+ "}";
		postRequest(uri, json, 400);
	}
	
	private void postRequest(URI uri, String json, int code) throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(code))
		.andExpect(result -> assertNotNull(result.getResponse()));
	}

	private String buildJson(String email) throws JsonProcessingException {
		return	"{"
				+ "\"username\":\"test_user\","
				+ "\"email\":\"" + email + "\","
				+ "\"password\":\"123456\","
				+ "\"roles\":["
					+ "\"ROLE_STUDENT\""
				+"]"
			+ "}";
	}
}
