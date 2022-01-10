package com.trainingquizzes.english.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Exercise;
import com.trainingquizzes.english.model.ExercisesQuantity;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.User;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
	
	@Query("select e from Exercise e join e.user u where u.username = :username")
	List<Exercise> findAllByUser(@Param("username")String username);
	
	@Query("select e from Exercise e where e.level = :level and e.subject = :subject and e.user = :user")
	List<Exercise> getExercisesByUserLevelAndSubject(@Param("user") User user, @Param("level") LevelType level, @Param("subject") Subject subject);
	
	@Query("select new com.trainingquizzes.english.model.ExercisesQuantity(e.user, e.level, e.subject, count(e) as quantity) " 
	+ "from Exercise e where e.user = :user and e.level = :level and e.subject = :subject "
    + "group by e.user, e.level, e.subject")
	Optional<ExercisesQuantity> getQuantityOfTheSameExercise(@Param("user") User user, @Param("level") LevelType level, @Param("subject") Subject subject);

}
