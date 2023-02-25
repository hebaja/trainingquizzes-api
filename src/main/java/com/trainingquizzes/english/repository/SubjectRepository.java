package com.trainingquizzes.english.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Quest;
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

	@Query("select s from Subject s where s.user.id = :userId and lower(s.title) like lower(concat('%', :query, '%'))")
	Page<Subject> findByUserIdAndTitleLikeIgnoreCase(@Param("query") String searchQuery, @Param("userId") Long userId, Pageable pagination);

	Page<Subject> findByTitleLikeIgnoreCaseAndUser(String string, User user, Pageable pagination);

	Page<Subject> findAllByUser(User user, Pageable pagination);
	
	Page<Subject> findPageableAllByIdIn(Set<Long> ids, Pageable pagination);

	Page<Subject> findByTitleLikeIgnoreCaseAndIdIn(String searchQuery, Set<Long> ids, Pageable pagination);

	@Query("select s from Subject s where s.user.id = :userId or s.id in :favoriteIds")
	Page<Subject> findAllAndFavoritesByUser(@Param("userId") Long userId, @Param("favoriteIds") Set<Long> favoriteSubjectsIds, Pageable pagination);
	
	@Query("select s from Subject s where lower(s.title) like lower(concat('%', :query, '%')) and (s.user.id = :userId or s.id in :favoriteIds)")
	Page<Subject> findByTitleLikeIgnoreCaseAndFavoritesByUser(@Param("query") String query, @Param("userId") Long userId, 
			@Param("favoriteIds") Set<Long> favoriteIds,Pageable pagination);
	
	@Query("select s from Subject s where lower(s.title) like lower(concat('%', :query, '%')) and ((s.user.id = :userId and s.publicSubject = true) or s.id in :favoriteIds)")
	Page<Subject> findByTitleLikeIgnoreCaseAndFavoritesByUserAndByPublicSubject(@Param("query") String query, @Param("userId") Long userId,
			@Param("favoriteIds") Set<Long> favoriteSubjectsIds, Pageable pagination);

	Page<Subject> findPublicByTitleLikeIgnoreCaseAndUser(String searchQuery, User user, Pageable pagination);

	Page<Subject> findByTitleLikeIgnoreCaseAndUserAndPublicSubject(String searchQuery, User user, boolean publicSubject, Pageable pagination);

	Page<Subject> findByTitleLikeIgnoreCaseAndPublicSubject(String searchQuery, boolean publicSubject, Pageable pagination);

	Page<Subject> findAllByPublicSubject(boolean publicSubject, Pageable pagination);

	Page<Subject> findByUserAndLevelAndPublicSubject(User user, LevelType levelType, boolean publicSubject, Pageable pageable);

	



}
