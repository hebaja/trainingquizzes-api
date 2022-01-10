package com.trainingquizzes.english.api;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SubjectRestTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturn200WhenRequestingSubjectsList() throws Exception {
		URI uri = new URI("/api/subject/all");
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(200));
	}

	@Test
	void shouldReturn200WhenRequestingSubjectById() throws Exception {
		URI uri = new URI("/api/subject/1");
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(200));
	}
	
	@Test
	void shouldReturn404WhenRequestingInexistentSubjectById() throws Exception {
		URI uri = new URI("/api/subject/10");
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(404));
	}
	
}
