package com.tweetapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tweetapp.security.JwtAuthorizationFilter;
import com.tweetapp.security.UserAuthentication;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	public static final String REGISTER_URL = "/api/v1.0/tweets/register";
	public static final String GET_ALL_POSTS_URL = "/api/v1.0/tweets/all";
	
	@Autowired
	private UserAuthentication authService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors();
		httpSecurity.csrf().disable().httpBasic().and().authorizeRequests()
				.antMatchers(REGISTER_URL).anonymous()
				.antMatchers(GET_ALL_POSTS_URL).anonymous()
				.antMatchers("/api/v1.0/tweets/updatePassword/**").anonymous()
//				.antMatchers("/api/v1.0/tweets/users").authenticated()
				.antMatchers("/api/v1.0/tweets/**").authenticated()
				.and()
				.addFilter(new JwtAuthorizationFilter(authenticationManager()));
	}
}