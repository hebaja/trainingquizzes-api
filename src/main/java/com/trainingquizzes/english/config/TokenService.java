package com.trainingquizzes.english.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.trainingquizzes.english.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${spring-english-training-quizzes-jwt-token-password}")
	private String tokenPassword;
	
	private static final Long TOKEN_EXPIRATION = 86400000L;

	public String generateToken(Authentication authentication) {
		
		User user = (User) authentication.getPrincipal();
		Date today = new Date();
		Date expirationDate = new Date(today.getTime() + TOKEN_EXPIRATION);
		
		return Jwts.builder()
				.setIssuer("Training Quizzes API")
				.setSubject(user.getId().toString())
				.setIssuedAt(today)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS256, tokenPassword)
				.compact();
	}

	public boolean isTokenValid(String token) {
		try {
			Jwts.parser().setSigningKey(tokenPassword).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long getUserId(String token) {
		Claims claims = Jwts.parser().setSigningKey(tokenPassword).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}
}
