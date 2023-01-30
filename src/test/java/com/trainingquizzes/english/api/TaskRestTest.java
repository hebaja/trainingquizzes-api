package com.trainingquizzes.english.api;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class TaskRestTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturn200AndListOfTasksBySubjectId() throws Exception {
		URI uri = new URI("/api/task?subjectId=1");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn204CaseSubjectIsNotFoundOrListOfTasksIsEmpty() throws Exception {
		URI uri = new URI("/api/task?subjectId=10");
		getRequest(uri, 204);
	}

	private void getRequest(URI uri, int code) throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(code));
	}
	
	
	

}
