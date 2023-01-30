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
class AverageRestTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturn200InCaseAveragesOfAnExistingUserAreFound() throws Exception {

		URI uri = new URI("/api/averages");
		
		String json = "{\"id\":\"2\",\"uid\":\"845751357545687899\",\"username\":\"henrique\",\"email\":\"henrique@hebaja.com\"}";
		
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(200));
		
	}

}
