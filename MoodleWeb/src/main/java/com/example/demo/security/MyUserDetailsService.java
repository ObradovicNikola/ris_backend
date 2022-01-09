package com.example.demo.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;

import model.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository ur;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> u = ur.findByEmail(email);
		u.orElseThrow(() -> new UsernameNotFoundException("Not found: " + email));
		UserDetails ud = new MyUserDetails(u.get());
		return ud;
	}
}
