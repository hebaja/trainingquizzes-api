package com.trainingquizzes.english.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.trainingquizzes.english.model.Quest;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.User;

@Transactional
public interface QuestRepository extends JpaRepository<Quest, Long>{

	Optional<Page<Quest>> findByUser(User user, Pageable pagination);

	@Query("select q from Quest q join q.subscribedUsersIds i on i = :userId")
	Optional<Page<Quest>> findBySubscribedUserId(@Param("userId")Long userId, Pageable pagination);
	
	@Query("select q from Quest q join q.subscribedUsersIds i on i = :userId and q.title like concat('%', lower(:query), '%')")
	Optional<Page<Quest>> findByTitleLikeIgnoreCaseAndSubscribedUserId(@Param("query") String searchQuery, @Param("userId")Long userId, Pageable pagination);

	void deleteAllBySubject(Subject subject);

	Optional<List<Quest>> findAllByUser(User user);

	Optional<Quest> findByPin(String pin);

	Optional<Page<Quest>> findByTitleLikeIgnoreCaseAndUser(String searchQuery, User user, Pageable pagination);
	
	

}
