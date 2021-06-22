package com.trainingquizzes.english.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
	
//	@Query("select t from TaskTest t join SubjectTest s where s.id = :subjectId")
//	Optional<List<TaskTest>> findAllBySubjectIds(@Param("subjectId") Long subjectId);
//
	@Query("select t from Task t where t.subject = :subject")
	Optional<List<Task>> findAllBySubject(@Param("subject") Subject subject);
	
	
//	@Query("select t from TaskTest t where t.subject_id = id")
//	Optional<List<Task>> findAllBySubjectId(@Param("id") Long id);

}
