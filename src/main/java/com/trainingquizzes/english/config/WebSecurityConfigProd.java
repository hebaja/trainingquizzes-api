package com.trainingquizzes.english.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.trainingquizzes.english.repository.UserRepository;

@Configuration
@EnableWebSecurity
@Profile({"prod", "local"})
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
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
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${spring-english-training-quizzes-default-domain}")
	private String defaultDomain;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable().authorizeRequests()
		.antMatchers(HttpMethod.GET, "/averages").authenticated()
		.antMatchers(HttpMethod.POST, "/api/averages", "/api/delete-user").authenticated()
		.antMatchers(HttpMethod.DELETE, "/api/subjects/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.GET, "/api/subjects").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT, "/api/subjects").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST, "/auth/**", "/api/user-register/**", "/api/reset-password/**").permitAll()
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