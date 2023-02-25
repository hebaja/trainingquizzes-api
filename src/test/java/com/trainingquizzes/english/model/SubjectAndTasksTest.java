package com.trainingquizzes.english.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.enums.Roles;

class SubjectAndTasksTest {
	
	private static Subject subject;

	@BeforeAll
	static void init() {
		Set<Authority> roles = new HashSet<Authority>();
		roles.add(new Authority(Roles.ROLE_TEACHER));
		Set<Account> accounts = new HashSet<Account>();
		accounts.add(new Account(AccountType.EMAIL));
		User user = createUser("user", "user@hebaja.com", roles, accounts);
	
		List<Task> tasks =  new ArrayList<>();
		
		for(int i = 0; i < 10; i++) {
			Task task = new Task();
			ArrayList<TaskOption> options = new ArrayList<>();
			for(int j = 0; j < 3; j++) {
				TaskOption taskOption = new TaskOption("option" + i + j, true);
				options.add(taskOption);
			}
			task.setPrompt("test_prompt" + i);			
			task.setOptions(options);
			tasks.add(task);
		}
		
		subject = new Subject("test_subject", tasks, user, LevelType.EASY, true);
	}
	
	private static User createUser(String name, String email, Set<Authority> roles, Set<Account> accounts) {
		return new User(name, email, "123456", true, roles, accounts);
	}
	
	@Test
	void shouldCreateSubject() {
		assertNotNull(subject);
		assertEquals("test_subject", subject.getTitle());
		assertEquals(10, subject.getTasks().size());
		assertEquals("user", subject.getUser().getUsername());
	}
	
	

}
