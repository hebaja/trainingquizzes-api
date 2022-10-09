package com.trainingquizzes.english.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.trainingquizzes.english.model.Quest;
import com.trainingquizzes.english.model.Trial;
import com.trainingquizzes.english.model.User;

@Transactional
public interface TrialRepository extends JpaRepository<Trial, Long>{

	@Modifying
	void deleteBySubscribedUser(User user);

	void deleteAllBySubscribedUserAndQuest(User user, Quest quest);

	Optional<Trial> findByQuest(Quest quest);

}
