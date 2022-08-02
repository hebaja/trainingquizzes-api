package com.trainingquizzes.english.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.trainingquizzes.english.repository.UserRepository;

@Configuration
@EnableWebSecurity
@Profile("test")
public class WebSecurityConfigTest extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${spring-english-training-quizzes-default-domain}")
	private String defaultDomain;
	
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String googleId;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/averages").authenticated()
		.antMatchers(HttpMethod.GET, "/redirect").permitAll()
		.antMatchers(HttpMethod.POST, "/api/averages", "/api/delete-user").permitAll()
		.antMatchers(HttpMethod.POST, "/auth/**", "/api/user-register/**", "/api/reset-password/**").permitAll()
		.antMatchers(HttpMethod.DELETE, "/api/subjects/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT, "/api/subjects").hasRole("ADMIN")
		.anyRequest().permitAll()
		.and().csrf().disable().headers().frameOptions().disable()
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().addFilterBefore(new AuthenticatioViaTokenFilter(tokenService, userRepository), UsernamePasswordAuthenticationFilter.class)
		.oauth2Login(oauth2 -> oauth2
		.redirectionEndpoint(redirection -> redirection
            .baseUri("/oauth2/callback/*")));
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/json/**", "/resources/css/**", "/resources/images/**", "/resources/js/**", "/files/json/**");
	}
	
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/api/english/**").allowedOrigins(defaultDomain).allowedMethods("GET", "POST");
//				registry.addMapping("/api/auth/**").allowedOrigins(defaultDomain).allowedMethods("POST");
//				registry.addMapping("/api/averages").allowedOrigins(defaultDomain).allowedMethods("POST");
//				registry.addMapping("/api/reset-password/**").allowedOrigins(defaultDomain).allowedMethods("POST");
//				registry.addMapping("/api/subjects/**").allowedOrigins(defaultDomain).allowedMethods("GET", "DELETE", "PUT");
//				registry.addMapping("/api/delete-user").allowedOrigins(defaultDomain).allowedMethods("POST");
//				registry.addMapping("/api/user-register/**").allowedOrigins(defaultDomain).allowedMethods("POST");
//			}
//		};
//	}
	
}