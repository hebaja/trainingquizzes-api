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
class UserRestTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturn200CaseUserIsFoundById() throws Exception {
		URI uri = new URI("/api/user?userId=2");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn204CaseNoUserIsFoundById() throws Exception {
		URI uri = new URI("/api/user?userId=10");
		getRequest(uri, 204);
	}
	
	@Test
	void shouldReturn400UserIdIsNullWhenRequestingUserById() throws Exception {
		URI uri = new URI("/api/user");
		getRequest(uri, 400);
	}
	
	@Test
	void shouldReturn200WhenUserInformationIsRequestedByUid() throws Exception {
		URI uri = new URI("/api/user/uid?uid=845751357545687899");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn204CaseThereIsNoUserByUid() throws Exception {
		URI uri = new URI("/api/user/uid?uid=111111111111111111");
		getRequest(uri, 204);
	}
	
	@Test
	void shouldReturn400CaseUserUidIsNullWhenRequestUserByUid() throws Exception {
		URI uri = new URI("/api/user/uid?uid");
		getRequest(uri, 400);
	}
	
	@Test
	void shouldReturn200WithContentTrueCaseUserExistsByEmail() throws Exception {
		URI uri = new URI("/api/user/email/henrique@hebaja.com");
		getRequestAndExpectString(uri, 200, "true");
	}
	
	@Test
	void shouldReturn200WithContentFalseCaseUserDoesNotExistByEmail() throws Exception {
		URI uri = new URI("/api/user/email/inexistent@hebaja.com");
		getRequestAndExpectString(uri, 200, "false");
	}
	
	@Test
	void shouldUpdateUserRolesAndReturn200() throws Exception {
		URI uri = new URI("/api/user/update-roles");
		String json = buildJson();
		postRequest(uri, json, 200);
	}
	
	@Test
	void shouldNotUpdateUserRolesAndReturn400CaseFormIsEmptyWhenRequestingUserUpdateRoles() throws Exception {
		URI uri = new URI("/api/user/update-roles");
		String json = "";
		postRequest(uri, json, 400);
	}
	
	@Test
	void shouldReturnPageWithAllTeachersWithResponseCode200() throws Exception {
		URI uri = new URI("/api/user/teachers?page=0&size=1");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturnQueriedPageWithAllTeachersWithResponseCode200() throws Exception {
		URI uri = new URI("/api/user/teachers?page=0&size=1&query=hebaja");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldDeleteUserByIdAndReturn200() throws Exception {
		URI uri = new URI("/api/user?userId=1");
		deleteRequest(uri, 200);
	}
	
	@Test
	void shouldNotDeleteUserByIdAndReturn400CaseUserIsNotFound() throws Exception {
		URI uri = new URI("/api/user?userId=10");
		deleteRequest(uri, 400);
	}

	private void getRequest(URI uri, int code) throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(code));
	}
	
	private void getRequestAndExpectString(URI uri, int code, String response) throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(code))
		.andExpect(MockMvcResultMatchers
				.content()
				.string(response));
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
	
	private void deleteRequest(URI uri, int code) throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.delete(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(code));
	}
	
	private String buildJson() {
		return "{"
				+ "\"id\":\"5\","
				+ "\"username\":\"pupil\","
				+ "\"email\":\"pupil@hebaja.com\","
				+ "\"password\":\"123456\","
				+ "\"roles\":["
				+ "\"ROLE_TEACHER\""
				+"]"
				+ "}";
	}
}
