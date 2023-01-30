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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class TrialRestTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldOpenTrialAndReturn200() throws Exception {
		URI uri = new URI("/api/trial/open");
		String json = buildOpenTrialFormJson(1, 1, 5);
		postRequest(uri, json, 200);
	}
	
	@Test
	void shouldReturn400CaseTrialIsNotFound() throws Exception {
		URI uri = new URI("/api/trial/open");
		String json = buildOpenTrialFormJson(99, 1, 4);
		postRequest(uri, json, 400);
	}
	
	@Test
	void shouldReturn400CaseUserIsNotFound() throws Exception {
		URI uri = new URI("/api/trial/open");
		String json = buildOpenTrialFormJson(1, 1, 10);
		postRequest(uri, json, 400);
	}

	@Test
	void shouldUpdateTrialAndReturn200() throws Exception {
		URI uri = new URI("/api/trial/update");
		String json = buildUpdateTrialFormJson(1L, true);
		postRequest(uri, json, 200);
	}
	
	@Test
	void shouldReturn412CaseTrialDataNotFound() throws Exception {
		URI uri = new URI("/api/trial/update");
		String json = buildUpdateTrialFormJson(3L, true);
		postRequest(uri, json, 412);
	}
	
	private String buildOpenTrialFormJson(long trialId, long trialNumber, long userId) {
		String json = "{"
				+ "\"trialId\":\"" + trialId + "\","
				+ "\"trialNumber\":\"" + trialNumber + "\","
				+ "\"userId\":\"" + userId + "\""
				+ "}";
		return json;
	}
	
	private String buildUpdateTrialFormJson(long id, boolean correct) {
		String json = "{"
				+ "\"id\":\"" + id + "\","
				+ "\"correct\":\"" + correct + "\""
				+ "}";
		return json;
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
	

}
