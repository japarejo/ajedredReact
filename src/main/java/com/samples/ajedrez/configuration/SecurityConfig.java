package com.samples.ajedrez.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.samples.ajedrez.service.JwtRequestFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig   {



	@Autowired
  	UserDetailsService usuarioDetailsService;


	  @Bean
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		  return authenticationConfiguration.getAuthenticationManager();
	  }

  	@Autowired
  	private JwtRequestFilter jwtRequestFilter;

  	@Bean
  	public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
  	}
	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
        //.httpBasic(withDefaults()) 
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/","/games/**","/api/login","/api/register", "/api/auth/**").permitAll()
		.antMatchers("/api/games/**").authenticated()
		.antMatchers("/api/player/**").authenticated()
        .anyRequest().authenticated()
		.and().cors()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioDetailsService);

	}



	
}