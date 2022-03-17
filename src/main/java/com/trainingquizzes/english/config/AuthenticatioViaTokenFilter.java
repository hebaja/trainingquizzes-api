package com.trainingquizzes.english.config;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.UserRepository;
import com.trainingquizzes.english.util.GoogleAuthenticationUtil;

public class AuthenticatioViaTokenFilter extends OncePerRequestFilter {
	
	private TokenService tokenService;
	
	private UserRepository repository;
	
	@Value("${spring-english-training-quizzes-facebook-access-token}")
	private String facebookAccessToken;
	

	public AuthenticatioViaTokenFilter(TokenService tokenService, UserRepository repository) {
		this.tokenService = tokenService;
		this.repository = repository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = recoverToken(request);
		
		if(tokenService.isTokenValid(token)) {
			authenticateClient(token);
		} else if(token != null) {
			GoogleIdTokenVerifier verifier = GoogleAuthenticationUtil.retrieveIdTokenVerifier();
			try {
				GoogleIdToken googleIdToken = verifier.verify(token);
				authenticateGoogleClient(googleIdToken.getPayload());
				
			} catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
				
				RestTemplate restTemplate = new RestTemplate();
		        String facebook = null;
		        
		        HttpHeaders headers = new HttpHeaders();
		        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				
				UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://graph.facebook.com/debug_token")
		                .queryParam("input_token", token).queryParam("access_token", facebookAccessToken);
				
				facebook = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
				
				String facebookUnescaped = StringEscapeUtils.unescapeJava(facebook.toString());
				
				System.out.println(facebookUnescaped);
				
				JSONObject obj = new JSONObject(facebookUnescaped);
				String userId = obj.getJSONObject("data").getString("user_id");
				authenticateFacebookCliente(userId);
			}
		}
		
		filterChain.doFilter(request, response);
	}

	private void authenticateFacebookCliente(String userId) {
		User user = repository.findByUid(userId).orElse(null);
		authenticate(user);
	}

	private void authenticateGoogleClient(Payload payload) {
		User user = repository.findByUid(payload.getSubject()).orElse(null);
		authenticate(user);
	}

	private void authenticateClient(String token) {
		Long idUser = tokenService.getUserId(token);
		User user = repository.findById(idUser).get();
		authenticate(user);
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
