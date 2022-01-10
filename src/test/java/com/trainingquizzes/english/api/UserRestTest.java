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
class UserRestTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturn200WhenUserInformationIsRequestedByUid() throws Exception {
		URI uri = new URI("/api/user/845751357545687899");
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(200));
	}
	
	@Test
	void shouldReturn404WhenInexistentUserInformationIsRequestedByUid() throws Exception {
		URI uri = new URI("/api/user/invalid");
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(404));
	}
	
	@Test
	void shouldReturn200WithContentTrueCaseUserExistsByEmail() throws Exception {
		URI uri = new URI("/api/user/email/henrique@hebaja.com");
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(200))
		.andExpect(MockMvcResultMatchers
				.content()
				.string("true"));
	}
	
	@Test
	void shouldReturn200WithContentFalseCaseUserDoesNotExistByEmail() throws Exception {
		URI uri = new URI("/api/user/email/inexistent@hebaja.com");
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(200))
		.andExpect(MockMvcResultMatchers
				.content()
				.string("false"));
	}
	
	@Test
	void shouldReturn200WhenUserWithSubjectsIsRequested() throws Exception {
		URI uri = new URI("/api/user/subjects");
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(200));
	}

}
