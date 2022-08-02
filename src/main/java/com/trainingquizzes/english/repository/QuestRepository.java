package com.trainingquizzes.english.repository;

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

	void deleteAllBySubject(Subject subject);

//	Optional<Page<Quest>> findBySubscribedUsersIds(User user, Pageable pagination);

//	void findAllByUserId(Long userId, Pageable pagination);

//	List<Quest> findByUser(User user);

}
