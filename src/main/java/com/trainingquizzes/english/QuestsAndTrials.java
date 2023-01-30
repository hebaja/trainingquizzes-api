//package com.trainingquizzes.english;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.HashSet;
//import java.util.Set;
//
//import com.trainingquizzes.english.model.Quest;
//import com.trainingquizzes.english.model.User;
//import com.trainingquizzes.english.repository.SubjectRepository;
//import com.trainingquizzes.english.repository.UserRepository;
//
//public class QuestsAndTrials {
//	
//	public static void generate(UserRepository userRepository, SubjectRepository subjectRepository) {
//		LocalDateTime startDate = LocalDateTime.now();
//		LocalDateTime finishDate = startDate.plusDays(6);
//		
//		User student1 = userRepository.findById(4L).orElse(null);
//		User student2 = userRepository.findById(5L).orElse(null);
//		User questOwner = userRepository.findById(2L).orElse(null);
//		
//		Set<User> students = new HashSet<>();
//		
//		students.add(student1);
//		students.add(student2);
//		
//		Quest quest = new Quest("test_quest", questOwner, questOwner.getSubjects().get(0), startDate, finishDate, 2, ChronoUnit.DAYS);
//		
//		students.forEach(student -> quest.subscribeUser(student));
//		
//		System.out.println("Quest title -> " + quest.getTitle());
//		System.out.println("Quest start at " + quest.getStartDate());
//		System.out.println("Quest finish at " + quest.getFinishDate());
//		
//		System.out.println("------------------------------------");
//		
//		quest.getTrials().forEach(trial -> {
//			System.out.println("Trial subject id -> " + trial.getQuest().getSubject().getId());
//			System.out.println("Trial user id -> " + trial.getSubscribedUser().getId());
//			System.out.println("Trial number -> " + trial.getTrialNumber());
//			System.out.println("Trial start at -> " + trial.getStartDate());
//			System.out.println("Trial is due at -> " + trial.getFinishDate());
//			System.out.println("*************************************");
//		});
//		
//	}
//}
