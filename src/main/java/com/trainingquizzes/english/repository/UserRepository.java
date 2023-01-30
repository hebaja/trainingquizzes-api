package com.trainingquizzes.english.repository;

import com.trainingquizzes.english.model.Average;
import com.trainingquizzes.english.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByUid(String uid);

	boolean existsByEmail(String email);
	
	@Query("select new com.trainingquizzes.english.model.Average(e.user.username, e.subject, e.level, round(avg(e.score), 1))"
			+ "from Exercise e where e.user.username = :username group by e.user, e.level, e.subject")
	List<Average> getAveragesByUsername(@Param("username")String username);
	
	@Query("select new com.trainingquizzes.english.model.Average(e.user.email, e.subject, e.level, round(avg(e.score), 1))"
			+ "from Exercise e where e.user.email= :email group by e.user.email, e.level, e.subject")
	List<Average> getAveragesByEmail(@Param("email")String email);
	
	@Query("select new com.trainingquizzes.english.model.Average(e.user.uid, e.subject, e.level, round(avg(e.score), 1))"
			+ "from Exercise e where e.user.uid= :uid group by e.user.uid, e.level, e.subject")
	List<Average> getAveragesByUid(@Param("uid")String uid);
	
	@Query("select new com.trainingquizzes.english.model.Average(e.user.id, e.subject, e.level, round(avg(e.score), 1))"
			+ "from Exercise e where e.user.id= :id group by e.user.id, e.level, e.subject")
	List<Average> getAveragesById(@Param("id")Long id);

	Page<User> findByUsernameLikeIgnoreCase(String searchQuery, Pageable pagination);

	@Query("select u from User u inner join u.roles ur on ur.role = 'ROLE_TEACHER' and (u.username like lower(:query) or u.email like lower(:query))")
	Page<User> findAllTeachersByUsernameAndEmailLikeIgnoreCase(@Param("query") String searchQuery, Pageable pagination);

	@Query("select u from User u inner join u.roles ur on ur.role = 'ROLE_TEACHER'")
	Page<User> findAllTeachers(Pageable pagination);

	@Query("select new com.trainingquizzes.english.model.Average(e.user.id, e.subject, e.level, round(avg(e.score), 1)) "
			+ "from Exercise e where e.user.id= :id group by e.user.id, e.level, e.subject")	
	Optional<Page<Average>> findPageableAveragesByUserId(@Param("id") Long id, Pageable pagination);

}
