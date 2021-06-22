package com.trainingquizzes.english.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trainingquizzes.english.token.UserRegisterToken;

public interface UserRegisterTokenRepository extends JpaRepository<UserRegisterToken, Long>{
	
	@Query("select t from UserRegisterToken t where t.token = :token")
	UserRegisterToken findByToken(@Param("token") String token);
	
	@Query("select t from UserRegisterToken t where t.email = :email")
	Optional<UserRegisterToken> findByEmail(String email);

	@Query("select t from UserRegisterToken t where t.email = :email")
	List<UserRegisterToken> findAllByEmail(String email);

}
