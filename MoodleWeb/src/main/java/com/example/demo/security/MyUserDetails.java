package com.example.demo.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.repository.RoleRepository;

public class MyUserDetails implements UserDetails {
	private String email;
	private String password;
	private boolean enabled;
	private Collection<GrantedAuthority> authorities;

	public MyUserDetails() {
		super();
	}

	public MyUserDetails(String email, String password, boolean enabled) {
		super();
		this.email = email;
		this.password = password;
		this.enabled = enabled;
	}

	public MyUserDetails(model.User u) {
		super();
		this.email = u.getEmail();
		this.password = u.getPassword();
		this.enabled = u.getEnabled();

		String role = u.getRole().getNaziv();
		this.authorities = new ArrayList<GrantedAuthority>();
		this.authorities.add(new SimpleGrantedAuthority(role));
//		this.authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enabled;
	}

}
