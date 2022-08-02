//package com.trainingquizzes.english.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.trainingquizzes.english.model.Student;
//import com.trainingquizzes.english.model.Teacher;
//import com.trainingquizzes.english.model.User;
//import com.trainingquizzes.english.repository.StudentRepository;
//import com.trainingquizzes.english.repository.TeacherRepository;
//import com.trainingquizzes.english.repository.UserRepository;
//
//@Service
//public class UserService {
//	
//	@Autowired
//	private UserRepository<Teacher> userRepository;
//	
//	@Autowired
//	private TeacherRepository teacherRepository;
//	
//	@Autowired
//	private StudentRepository studentRepository;
//	
//	public Teacher findTeacherById(Long id) {
//		return teacherRepository.findById(id).orElse(null);
//	}
//	
//	public Teacher findTeacherByEmail(String email) {
//		return teacherRepository.findByEmail(email).orElse(null);
//	}
//	
//	public Student findStudentById(Long id) {
//		return studentRepository.findById(id).orElse(null);
//	}
//	
//	public User findById(Long id) {
//		return userRepository.findById(id).orElse(null);
//	}
//	
//	public User save(User user) {
//		return userRepository.save(user);
//	}
//	
//	public User findByEmail(String email) {
//		return userRepository.findByEmail(email).orElse(null);
//	}
//	
//	public boolean existsByEmail(String email) {
//		return userRepository.existsByEmail(email);
//	}
//	
//	public void delete(User user) {
//		userRepository.delete(user);
//	}
//
//	public User findByUid(String uid) {
//		return userRepository.findByUid(uid);
//	}
//
//	
//
//
//
//}
