package com.trainingquizzes.english.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.User;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
	
	@Query("select s from Subject s where s.user = :user")
	Optional<List<Subject>> findAllByUser(@Param("user") User user);
	
	@Query("select s from Subject s where s.level = :level")
	Optional<List<Subject>> findAllByLevel(@Param("level") LevelType level);

	Optional<List<Subject>> findAllByUserId(Long id);
	
	Page<Subject> findPageableByuserId(Long userId, Pageable pagination);

	Page<Subject> findByTitleLikeIgnoreCase(String searchQuery, Pageable pagination);

	@Query("select s from Subject s where s.user.id = :userId and s.level = :level")
	Page<Subject> findPageableByUserIdAndLevel(@Param("userId") Long userId, @Param("level") LevelType level, Pageable pageable);

	@Query("select s from Subject s where s.user.id = :userId and s.title like concat('%', lower(:query), '%')")
	Page<Subject> findByUserIdAndTitleLikeIgnoreCase(@Param("query") String searchQuery, @Param("userId") Long userId, Pageable pagination);

	Page<Subject> findByTitleLikeIgnoreCaseAndUser(String string, User user, Pageable pagination);

	
	
}
