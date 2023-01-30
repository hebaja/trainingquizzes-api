package com.trainingquizzes.english.api;

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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class PasswordResetRestTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturn200CaseEmailExistsInDB() throws Exception {
		URI uri = new URI("/api/reset-password/request");
		String json = "{\"id\":\"2\",\"username\":\"user\",\"email\":\"henrique@hebaja.com\",\"password\":\"111711\"}";
		mockRequest(uri, json, 200);
	}
	
	@Test
	void shouldReturn404CaseEmailIsNotFound() throws Exception {
		URI uri = new URI("/api/reset-password/request");
		String json = "{\"id\":\"2\",\"username\":\"user\",\"email\":\"henrique@henrique.com\",\"password\":\"111711\"}";
		mockRequest(uri, json, 404);
	}
	
	@Test
	void shouldReturn200CaseTokenIsFound() throws Exception {
		URI uri = new URI("/api/reset-password/validate-token");
		String json = "{\"token\":\"3046b21e-1509-4bc1-b643-c81ce9ea0e6c\"}";
		mockRequest(uri, json, 200);
	}
	
	@Test
	void shouldReturn400CaseTokenIsNotFound() throws Exception {
		URI uri = new URI("/api/reset-password/validate-token");
		String json = "{\"token\":\"3046b21e-1509-4bc1-b643-c81ce9edfdfdf\"}";
		mockRequest(uri, json, 400);
	}
	
	@Test
	void shouldReturn200AndResetPasswordCaseTokenIsValid() throws Exception {
		URI uri = new URI("/api/reset-password");
		String json = "{\"token\":\"3046b21e-1509-4bc1-b643-c81ce9ea0e6c\",\"password\":\"123456\"}";
		mockRequest(uri, json, 200);
	}
	
	@Test
	void shouldReturn400AndNotReturnPasswordCaseTokenIsInvalid() throws Exception {
		URI uri = new URI("/api/reset-password");
		String json = "{\"token\":\"3046xx2oe-1q09-4bc1-b643-c11cegea0e6c\",\"password\":\"123456\"}";
		mockRequest(uri, json, 400);
	}

	private void mockRequest(URI uri, String json, int code) throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(code));
	}
}
