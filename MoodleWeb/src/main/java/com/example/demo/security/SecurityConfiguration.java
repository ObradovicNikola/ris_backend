package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private AuthorizeFilter authorizeFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//		auth.inMemoryAuthentication()
//			.withUser("user")
//			.password("user")
//			.roles("USER")
//			.and()
//			.withUser("foo")
//			.password("foo")
//			.roles("ADMIN");

		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()).and()
//			.cors().and()
				.csrf().disable().authorizeRequests()
				.antMatchers("/login", "/register", "/").permitAll()
				.antMatchers("/admin").hasAnyAuthority("ADMIN")
				.antMatchers("/courses/signup").hasAnyAuthority("STUDENT")
				.antMatchers("/uploadFile", "/deleteFile/**", "/course/**").hasAnyAuthority("PROFESOR", "ADMIN")
				.antMatchers("/courses/*").hasAnyAuthority("STUDENT", "PROFESOR", "ADMIN")
				.antMatchers("/courses").hasAnyAuthority("STUDENT", "ADMIN")
				.antMatchers("/mycourses").hasAnyAuthority("STUDENT", "PROFESOR")
				.anyRequest().authenticated().and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authorizeFilter, UsernamePasswordAuthenticationFilter.class);
	}
//			.antMatchers("/user").hasAnyRole("STUDENT", "PROFESOR", "ADMIN")

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
