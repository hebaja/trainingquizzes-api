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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainingquizzes.english.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class SubjectRestTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Test
	void shouldReturn200WhenRequestingSubjectsList() throws Exception {
		URI uri = new URI("/api/subject/all");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn200WhenRequestingSubjectById() throws Exception {
		URI uri = new URI("/api/subject?subjectId=1");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn404WhenRequestingInexistentSubjectById() throws Exception {
		URI uri = new URI("/api/subject?subjectId=10");
		getRequest(uri, 404);
	}
	
	@Test
	void shouldReturnSubjectsByLevel() throws Exception {
		URI uri = new URI("/api/subject/level?level=EASY");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturnSubjectsByUser() throws Exception {
		URI uri = new URI("/api/subject/user?userId=2");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturnSubjectsByUserAndByLevel() throws Exception {
		URI uri = new URI("/api/subject/user?userId=2&level=EASY");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn400CaseUserIdIsNull() throws Exception {
		URI uri = new URI("/api/subject/user?level=EASY");
		getRequest(uri, 400);
	}
	
	@Test
	void shouldReturnSubjectsReducedList() throws Exception {
		URI uri = new URI("/api/subject/reduced-list?page=0&size=1&sort=creationDate,desc");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturnQueriedSubjectsReducedList() throws Exception {
		URI uri = new URI("/api/subject/reduced-list?query=com&page=0&size=1&sort=creationDate,desc");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturnSubjectsReducedListByUserId() throws Exception {
		URI uri = new URI("/api/subject/reduced-list?userId=2&page=0&size=1&sort=creationDate,desc");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturnQueriedSubjectsReducedListByUserId() throws Exception {
		URI uri = new URI("/api/subject/reduced-list?userId=2&query=com&page=0&size=1&sort=creationDate,desc");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturnPageableSubjectsByTeacher() throws Exception {
		URI uri = new URI("/api/subject/pageable-teacher?userId=2&page=0&size=1&sort=creationDate,desc");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn400CaseUserIdIsNullWhenRequestPageableSubjectsByTeacher() throws Exception {
		URI uri = new URI("/api/subject/pageable-teacher?page=0&size=1&sort=creationDate,desc");
		getRequest(uri, 400);
	}
	
	@Test
	void shouldReturnSubjectsListByTeacher() throws Exception {
		URI uri = new URI("/api/subject/teacher?id=2");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn204CaseSubjectsListIsEmpty() throws Exception {
		URI uri = new URI("/api/subject/teacher?id=4");
		getRequest(uri, 204);
	}
	
	@Test
	void shouldReturn400CaseUserIdIsNullWhenRequestingSubjectsListByTeacher() throws Exception {
		URI uri = new URI("/api/subject/teacher");
		getRequest(uri, 400);
	}
	
	@Test
	void shouldReturnSubjectsReducedListByTeacher() throws Exception {
		URI uri = new URI("/api/subject/teacher/reduced-list?id=2");
		getRequest(uri, 200);
	}
	
	@Test
	void shouldReturn204CaseReducedSubjectsListIsEmptyByTeacher() throws Exception {
		URI uri = new URI("/api/subject/teacher/reduced-list?id=4");
		getRequest(uri, 204);
	}
	
	@Test
	void shouldReturn400CaseUserIdIsNullWhenRequestingReducedSubjectsListByTeacher() throws Exception {
		URI uri = new URI("/api/subject/teacher/reduced-list");
		getRequest(uri, 400);
	}
	
	@Test
	void shouldDeleteSubjectBySubjectId() throws Exception {
		URI uri = new URI("/api/subject?subjectId=5");
		deleteRequest(uri, 200);
	}
	
	@Test 
	void shouldReturn400CaseSubjectIsNotFoundWhenRemovingSubject() throws Exception {
		URI uri = new URI("/api/subject?subjectId=10");
		deleteRequest(uri, 400);
	}
	
	// TODO NEED TO CHECK HOW TO HANDLE THE WAY USERFORM RECEIVES LIST OF AUTHORITIES
//	@Test 
//	void shouldRegisterNewSubject() throws Exception {
//		URI uri = new URI("/api/subject");
//		List<Task> tasks = new ArrayList<Task>();
//		
//		List<Authority> roles = new ArrayList<>();
//		roles.add(new Authority(Roles.ROLE_TEACHER));
//		
//		List<Account> accounts = new ArrayList<>();
//		accounts.add(new Account(AccountType.EMAIL));
//		
//		User user = new User("test_user", "test@test.com", "123456", true, roles, accounts);
//				
//		User savedUser = userRepository.save(user);
//		
//		for(int i = 0; i < 3; i++) {
//			Task task = new Task();
//			task.setPrompt("prompt" + i);
//			
//			List<TaskOption> options = new ArrayList<>();
//			
//			for(int j = 0; j < 3; j++) {
//				TaskOption option = new TaskOption("option_prompt" + j, true);
//				options.add(option);
//			}
//			
//			task.setOptions(options);
//			tasks.add(task);
//		}
//		
//		
//		Subject subject = new Subject();
//		subject.setTitle("testing_subject");
//		subject.setLevel(LevelType.EASY);
//		subject.setUser(savedUser);
//		subject.setTasks(tasks);
//		
//		String json = mapper.writeValueAsString(subject);
//						
//		putRequest(uri, json, 200);
//	}
	
	private void getRequest(URI uri, int code) throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(code));
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
	
	private void deleteRequest(URI uri, int code) throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.delete(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(code))
		.andExpect(result -> assertNotNull(result.getResponse()));
	}
	
}
