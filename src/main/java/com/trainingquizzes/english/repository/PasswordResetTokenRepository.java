package com.trainingquizzes.english.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trainingquizzes.english.token.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	
	@Query("select t from PasswordResetToken t where t.token = :token")
	Optional<PasswordResetToken> findByToken(@Param("token") String token);
	
	@Query("select t from PasswordResetToken t where t.user.id = :id")
	Optional<PasswordResetToken> findByUserId(Long id);

	@Query("select t from PasswordResetToken t where t.user.id = :id")
	List<PasswordResetToken> findAllByUserId(Long id);

}
