package com.trainingquizzes.english.quest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.trainingquizzes.english.enums.AccountType;
import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.enums.Roles;
import com.trainingquizzes.english.model.Account;
import com.trainingquizzes.english.model.Authority;
import com.trainingquizzes.english.model.Quest;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.model.TemporaryTrialDataStore;
import com.trainingquizzes.english.model.User;

class QuestTest {
	
	static private Quest quest;
	static private Set<User> students = new HashSet<User>();
	static private List<TemporaryTrialDataStore> temporaryTrialDataStoreList = new ArrayList<>();
	static private double[] scores = new double[10];
	
	@BeforeAll
	static void init() {
		fetchStudents();
		List<Task> tasks = new ArrayList<Task>();
		List<Authority> roles = new ArrayList<Authority>();
		roles.add(new Authority(Roles.ROLE_TEACHER));
		List<Account> accounts = new ArrayList<Account>();
		accounts.add(new Account(AccountType.EMAIL));
		User owner = createUser("owner", "owner@hebaja.com", roles, accounts);
		
		LocalDateTime now = LocalDateTime.now();
		
		quest = new Quest(
				"test_quest",
				owner,
				new Subject("test_subject", tasks, owner, LevelType.EASY),
				now,
				now.plusDays(5), 
				5,
				ChronoUnit.DAYS
				);
		
		students.forEach(student -> quest.subscribeUser(student));
		
		quest.getTrials().forEach(trial -> {
			temporaryTrialDataStoreList.add(new TemporaryTrialDataStore(trial, trial.getSubscribedUser(), trial.getTrialNumber(), null));
		});
		
		generateScores();
		
		setTrialsScore();
		
		
		
	}


	private static void fetchStudents() {
		List<Authority> roles = new ArrayList<Authority>();
		roles.add(new Authority(Roles.ROLE_STUDENT));
		List<Account> accounts = new ArrayList<Account>();
		accounts.add(new Account(AccountType.EMAIL));
		User user1 = createUser("user1", "student1@hebaja.com", roles, accounts);
		User user2 = createUser("user2", "student2@hebaja.com", roles, accounts);
		students.add(user1);
		students.add(user2);
	}

	private static User createUser(String name, String email, List<Authority> roles, List<Account> accounts) {
		return new User(name, email, "123456", true, roles, accounts);
	}
	
	private static void generateScores() {
		Random random = new Random();
		double minRange = 0;
		double maxRange = 10;
				
		for (int i = 0; i < 10; i++) {
			scores[i] = minRange = (maxRange - minRange) * random.nextDouble();
		}
	}
	
	private static void setTrialsScore() {
		for (int i = 0; i < quest.getTrials().size(); i++) {
			quest.getTrials().get(i).setScore(scores[i]);
		}
	}
	
	@Test
	void shouldCreateAQuest() {
		assertNotNull(quest);
	}
	
	@Test
	void shouldSubscribeStudents() {
		assertEquals(2, quest.getSubscribedUsersIds().size());
	}
	
	@Test
	void shouldCreateTrials() {
		assertEquals(10, quest.getTrials().size());
	}
	
	@Test
	void intervalBetweenStartAndFinishDateShouldMatch() {
		quest.getTrials().forEach(trial -> {
			assertEquals(true, trial.getStartDate().equals(trial.getFinishDate().minusDays(1).plus(50, ChronoUnit.MILLIS)));
		});
	}
	
	@Test
	void shouldSetTrialScoreIfItIsBeforeDueDate() {
		assertTrue(quest.getTrials().get(0).setScore(7.0));
	}
	
	@Test
	void shouldSetTrialsScore() {
		for (int i = 0; i < quest.getTrials().size(); i++) {
			assertEquals(scores[i], quest.getTrials().get(i).getScore());
		}
	}
	
	@Test
	void shouldCreateTemporaryTrialDataStoreObjects() {
		assertEquals(10, temporaryTrialDataStoreList.size());
	}
	
	//FINISH TRIAL AND TEMPORARY
	//REMOVE TEMPORARY TRIAL DATA
	
	@Test
	void shouldFinishTrials() {
		
	}
		
}
