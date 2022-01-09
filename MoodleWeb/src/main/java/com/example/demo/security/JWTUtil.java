package com.example.demo.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTUtil {

	@Value("${secret_key}")
	private String secret;

	public String generateToken(UserDetails user) {
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder().setClaims(claims).setSubject(user.getUsername())
				// valid for 7 days
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

//	public String extractUsername(String token) {
//		return extractAllClaims(token).getSubject();
//	}
	
	public String extractEmail(String token) {
		return extractAllClaims(token).getSubject();
	}

	public boolean validateToken(String token, UserDetails user) {
		return extractEmail(token).equals(user.getUsername());

	}
}