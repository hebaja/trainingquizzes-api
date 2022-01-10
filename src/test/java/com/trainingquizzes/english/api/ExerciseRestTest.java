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
class ExerciseRestTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturn200WhenSavingExercisesListOfAnExistingUser() throws Exception {
		URI uri = new URI("/api/exercise");
		
		String json = "[\n"
				+ "    {\n"
				+ "        \"userUid\":\"845751357545687899\",\n"
				+ "        \"subjectId\":\"1\",\n"
				+ "        \"level\":\"EASY\",\n"
				+ "        \"score\":\"8\"\n"
				+ "    },\n"
				+ "    {\n"
				+ "        \"userUid\":\"845751357545687899\",\n"
				+ "        \"subjectId\":\"2\",\n"
				+ "        \"level\":\"MEDIUM\",\n"
				+ "        \"score\":\"7\"\n"
				+ "    }\n"
				+ "]";
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
