package com.trainingquizzes.english.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.trainingquizzes.english.oauth.FacebookOAuth2UserService;
import com.trainingquizzes.english.oauth.GoogleOidcUserService;

@Configuration
@EnableWebSecurity
@Profile("local_ssl")
public class WebSecurityConfigLocalSsl extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private GoogleOidcUserService googleOidcUserService;
	
	@Autowired
	private FacebookOAuth2UserService facebookOAuth2Service;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.requiresChannel()
			.anyRequest()
			.requiresSecure()
		.and()
			.headers().frameOptions().disable()
			.and()
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/**").permitAll()
			.and()		
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/english/quiz")
				.failureUrl("/login?error=Wrong+username+or+password")
				.permitAll()
			.and()
				.oauth2Login(oauth2 -> oauth2
						.loginPage("/login")
						.defaultSuccessUrl("/english/quiz")
						.redirectionEndpoint(redirection -> redirection
			                .baseUri("/oauth2/callback/*")
			            )
						.userInfoEndpoint(userInfo -> userInfo
							.oidcUserService(googleOidcUserService)
							.userService(facebookOAuth2Service)
						)
					)
				.logout()
				.logoutSuccessUrl("/login?logout=You+have+been+signed+out")
				.permitAll();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(createAuthenticationProvider());
	}
	
	@Bean
	public DaoAuthenticationProvider createAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/json/**", "/resources/css/**", "/resources/images/**", "/resources/js/**", "/files/json/**");
	}
	
	
	
}
