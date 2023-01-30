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
class ExerciseRestTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturn200WhenSavingExercisesListOfAnExistingUser() throws Exception {
		URI uri = new URI("/api/exercise/save");
		
		String json = "{\n"
				+ "        \"userId\":\"4\",\n"
				+ "        \"subjectId\":\"1\",\n"
				+ "        \"score\":\"8\"\n"
				+ "    }\n";
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
