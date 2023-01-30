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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.trainingquizzes.english.repository.UserRepository;

@Configuration
@EnableWebSecurity
@Profile({"prod", "local"})
public class WebSecurityConfigProd extends WebSecurityConfigurerAdapter {
	
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
	
	private static final String TEACHER = "TEACHER";
	private static final String STUDENT = "STUDENT"; 

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable().authorizeRequests()
		.antMatchers(HttpMethod.GET, "/api/schedule/**").permitAll()
		.antMatchers(HttpMethod.POST, "/auth/**", "/api/user-register/**", "/api/reset-password/**", "/api/schedule/**").permitAll()
		.antMatchers(HttpMethod.POST, "/api/delete-user").authenticated()
		.antMatchers(HttpMethod.DELETE, "/api/user").authenticated()
		.antMatchers(HttpMethod.GET, "/api/averages").hasRole(STUDENT)
		.antMatchers(HttpMethod.DELETE, "/api/subject/**").hasRole(TEACHER)
		.antMatchers(HttpMethod.DELETE, "/api/quest").hasRole(TEACHER)
		.antMatchers(HttpMethod.PUT, "/api/subjects").hasRole(TEACHER)
		.anyRequest().permitAll()
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
	
}