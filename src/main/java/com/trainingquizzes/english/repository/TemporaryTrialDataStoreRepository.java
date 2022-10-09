package com.trainingquizzes.english.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.trainingquizzes.english.model.Trial;
import com.trainingquizzes.english.model.TemporaryTrialDataStore;
import com.trainingquizzes.english.model.User;

@Transactional
public interface TemporaryTrialDataStoreRepository extends JpaRepository<TemporaryTrialDataStore, Long> {

	Optional<TemporaryTrialDataStore> findByUser(User user);

	@Query("select t from TemporaryTrialDataStore t where t.trial = :trial and t.user = :user and t.trialNumber = :trialNumber")
	Optional<TemporaryTrialDataStore> findByTrialUserAndTrialNumber(@Param("trial") Trial trial, @Param("user") User user, @Param("trialNumber") int trialNumber);
	
	@Modifying
	@Query("delete from TemporaryTrialDataStore t where t.questId = :questId")
	void deleteAllByQyestId(@Param("questId") Long questId);

	void deleteAllByUser(User user);

	Optional<List<TemporaryTrialDataStore>> findAllByUser(User user);

	void deleteByTrial(Trial trial);
}
