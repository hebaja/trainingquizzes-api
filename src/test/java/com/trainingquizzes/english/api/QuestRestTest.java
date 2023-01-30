package com.trainingquizzes.english.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.time.LocalDateTime;

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
import org.threeten.bp.ZoneId;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class QuestRestTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturn200CaseQuestIsFoundByQuestId() throws Exception {
		URI uri = new URI("/api/quest?questId=1");
		getRequest(uri, 200);
	}
		
	@Test
	void shouldReturn200CaseQuestIsFoundByQuestIdAndByUserId() throws Exception {
		URI uri = new URI("/api/quest?questId=1&userId=2");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn400CaseQuestIsNotFound() throws Exception {
		URI uri = new URI("/api/quest?questId=10");
		getRequest(uri, 400);
	}
	
	@Test 
	void shouldReturn400CaseUserIsNotFound() throws Exception {
		URI uri = new URI("/api/quest?questId=10&userId=10");
		getRequest(uri, 400);
	}
	
	@Test
	void shouldReturn200CaseQuestIsFoundByPin() throws Exception {
		URI uri = new URI("/api/quest/pin?pin=44444");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn400CaseQuestIsNotFoundByPin() throws Exception {
		URI uri = new URI("/api/quest/pin?pin=11122");
		getRequest(uri, 400);
	}
	
	@Test
	void shouldReturn200CaseQuestsAreFoundByUserId() throws Exception {
		URI uri = new URI("/api/quest/created-quests?userId=2&page=0&size=2&sort=startDate,desc");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn400WhenRequestingQuestsCaseNoUserIsFound() throws Exception {
		URI uri = new URI("/api/quest/created-quests?userId=10&page=0&size=2&sort=startDate,desc");
		getRequest(uri, 400);
	}
	
	@Test
	void shouldReturn200CaseQuestsListIsEmptyByUserId() throws Exception {
		URI uri = new URI("/api/quest/created-quests?userId=2&page=0&size=2&sort=startDate,desc");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn200CaseSubscribedQuestsAreFoundByUserId() throws Exception {
		URI uri = new URI("/api/quest/subscribed-quests?userId=4&page=0&size=2&sort=startDate,desc");
		getRequest(uri, 200);
	}
		
	@Test
	void shouldReturn400WhenRequestingSubscribedQuestsCaseUserIsNotFound() throws Exception {
		URI uri = new URI("/api/quest/subscribed-quests?userId=10&page=0&size=2&sort=startDate,desc");
		getRequest(uri, 400);
	}
	
	@Test
	void shouldReturn200CaseSubscribedQuestsListIsEmptyByUserId() throws Exception {
		URI uri = new URI("/api/quest/subscribed-quests?userId=4&page=0&size=2&sort=startDate,desc");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldRegisterQuestAndReturn200() throws Exception {
		URI uri = new URI("/api/quest");
		String json = generateQuestJson(1, 2);
		putRequest(uri, json, 200);
	}
	
	@Test
	void shouldReturn400CaseUserIsNullWhenTryingToRegisterQuest() throws Exception {
		URI uri = new URI("/api/quest");
		String json = generateQuestJson(1, 0);
		putRequest(uri, json, 400);
	}
	
	@Test
	void shouldReturn400CaseSubjectIsNullWhenTryingToRegisterQuest() throws Exception {
		URI uri = new URI("/api/quest");
		String json = generateQuestJson(0, 2);
		putRequest(uri, json, 400);
	}
	
	@Test
	void shouldDeleteQuestAndReturn200ByQuestId() throws Exception {
		// IT SEEMS IT IS NOT POSSIBLE TO DELETE ID 1 BECAUSE IT HAS BEEN DELETED BEFORE
		URI uri = new URI("/api/quest/2");
		mockMvc
		.perform(MockMvcRequestBuilders
				.delete(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(200))
		.andExpect(result -> assertNotNull(result.getResponse()));
	}
	
	@Test
	void shouldFinishQuestByIdAndReturn200() throws Exception {
		URI uri = new URI("/api/quest/finish?questId=3");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn400CaseQuestIdIsNotFoundWhenTryingToFinish() throws Exception {
		URI uri = new URI("/api/quest/finish?questId=10");
		getRequest(uri, 400);
	}
	
	@Test 
	void shouldReturn400CaseQuestIdIsNullWhenTryingToFinish() throws Exception {
		URI uri = new URI("/api/quest/finish");
		getRequest(uri, 400);
	}
	
	@Test
	void shouldSubscribeUserToQuestAndReturn200() throws Exception {
		URI uri = new URI("/api/quest/subscribe");
		String json = "{"
				+ "\"questId\":\"" + 4 + "\","
				+ "\"userId\":\"" + 4 + "\""
				+ "}";
		postRequest(uri, json, 200);
	}
	
	@Test
	void shouldReturn400CaseQuestIsNotPresentWhenSubscribingToQuest() throws Exception {
		URI uri = new URI("/api/quest/subscribe");
		String json = "{"
				+ "\"questId\":\"" + 10 + "\","
				+ "\"userId\":\"" + 4 + "\""
				+ "}";
		postRequest(uri, json, 400);
	}
	
	@Test
	void shouldReturn400CaseUserIsNotPresentWhenSubscribingToQuest() throws Exception {
		URI uri = new URI("/api/quest/subscribe");
		String json = "{"
				+ "\"questId\":\"" + 4 + "\","
				+ "\"userId\":\"" + 10 + "\""
				+ "}";
		postRequest(uri, json, 400);
	}
	
	@Test
	void shouldUnsubscribeUserFromQuestAndReturn200() throws Exception {
		URI uri = new URI("/api/quest/unsubscribe-user?userId=5&questId=4");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn400CaseUserIsNotPresentWhenUnsubscribingUser() throws Exception {
		URI uri = new URI("/api/quest/unsubscribe-user?userId=10&questId=4");
		getRequest(uri, 400);
	}
	
	@Test
	void shouldReturn400CaseQuestIsNotPresentWhenUnsubscribingUser() throws Exception {
		URI uri = new URI("/api/quest/unsubscribe-user?userId=4&questId=10");
		getRequest(uri, 400);
	}
			
	private void getRequest(URI uri, int code) throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(code))
		.andExpect(result -> assertNotNull(result.getResponse()));
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
	
	private void putRequest(URI uri, String json, int code) throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.put(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(code));
	}
	
	private String generateQuestJson(long subjectId, long userId) {
		String today = LocalDateTime.now().toString();
		String tomorrow = LocalDateTime.now().plusDays(1L).toString();
		String timeZone = ZoneId.systemDefault().toString();
		
		return "{"
				+ "\"title\":\"test_quest\","
				+ "\"startDate\":\"" + today + "\","
				+ "\"finishDate\":\"" + tomorrow + "\","
				+ "\"timeZone\":\"" + timeZone +"\","
				+ "\"timeInterval\":\"1\","
				+ "\"subjectId\":\"" + subjectId + "\","
				+ "\"userId\":\"" + userId + "\""
				+ "}";
	}

}
