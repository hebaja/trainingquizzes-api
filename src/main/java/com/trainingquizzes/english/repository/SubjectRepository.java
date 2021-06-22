package com.trainingquizzes.english.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.User;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
	
	@Query("select s from Subject s where s.user = :user")
	Optional<List<Subject>> findAllByUser(@Param("user") User user);

}
