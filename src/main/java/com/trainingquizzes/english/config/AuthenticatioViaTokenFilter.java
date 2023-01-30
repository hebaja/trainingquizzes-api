package com.trainingquizzes.english.config;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.util.GoogleAuthenticationUtil;

public class AuthenticatioViaTokenFilter extends OncePerRequestFilter {
	
	private TokenService tokenService;
	
	private UserRepository repository;

	public AuthenticatioViaTokenFilter(TokenService tokenService, UserRepository repository) {
		this.tokenService = tokenService;
		this.repository = repository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = recoverToken(request);
		GoogleIdTokenVerifier verifier = GoogleAuthenticationUtil.retrieveIdTokenVerifier();
		
		if(token != null) {
			try {
				GoogleIdToken googleIdToken = verifier.verify(token);
				if(googleIdToken != null) authenticateGoogleClient(googleIdToken.getPayload().getEmail());
				else if(tokenService.isTokenValid(token)) authenticateClient(token);
				else response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			} catch (GeneralSecurityException | IOException e) {
				e.printStackTrace();
			}
		}
		
		filterChain.doFilter(request, response);
	}

	private void authenticateGoogleClient(String email) {
		User user = repository.findByEmail(email).orElse(null);
		if(user != null) {
			authenticate(user);
		}
	}

	private void authenticateClient(String token) {
		Long idUser = tokenService.getUserId(token);
		Optional<User> userOptional = repository.findById(idUser);
		if(userOptional.isPresent()) authenticate(userOptional.get());
	}

	private void authenticate(User user) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String recoverToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.substring(7, token.length());
	}
}
