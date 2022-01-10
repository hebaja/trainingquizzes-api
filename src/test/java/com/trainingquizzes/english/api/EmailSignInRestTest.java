package com.trainingquizzes.english.api;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class EmailSignInRestTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturn404InCaseUserIsNotFound() throws Exception {
		URI uri = new URI("/api/user/email");
		String json = "{\"uid\":\"000\",\"username\":\"invalid\",\"email\":\"invalid@hebaja.com\",\"password\":\"123456\"}";
		
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(404));
	}
	
	@Test
	void shouldReturn200InCaseUserIsFound() throws Exception {
		URI uri = new URI("/api/user/email");
		String json = "{\"uid\":\"000\",\"username\":\"hebaja\",\"email\":\"hebaja@hebaja.com\",\"password\":\"123456\"}";
		
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(200));
	}

}
