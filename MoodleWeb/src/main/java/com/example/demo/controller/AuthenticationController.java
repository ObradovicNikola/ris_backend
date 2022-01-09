package com.example.demo.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.email.EmailServiceImpl;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.dto.Message;
import com.example.demo.repository.dto.UserDTO;
import com.example.demo.security.JWTUtil;
import com.example.demo.security.MyUserDetailsService;
import com.example.demo.security.models.AuthenticationRequest;
import com.example.demo.security.models.AuthenticationResponse;
import com.example.demo.security.models.RegistrationRequest;

import model.User;

@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	EmailServiceImpl emailService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
					authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			e.printStackTrace();
			Message message = new Message("Wrong email or password");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

		final String jwt = jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) throws Exception {

		String message = "";
		ResponseEntity<String> re;

		if (userRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
			message = "Email is already registered";
			return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(message);
		}

		// ne dozvoli registraciju nezeljenih uloga
		if (registrationRequest.getIdRole() != 2 && registrationRequest.getIdRole() != 3) {
			message = "Undefined role";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}

		User u = new User();
		u.setEmail(registrationRequest.getEmail());
		u.setPassword(bCryptPasswordEncoder.encode(registrationRequest.getPassword()));
		u.setIme(registrationRequest.getIme());
		u.setPrezime(registrationRequest.getPrezime());
		u.setRole(roleRepo.findById(registrationRequest.getIdRole()).get());
		u.setEnabled(false);
//		u.setEnabled(true);

		try {
			userRepo.save(u);
			message = "Thanks for registering. Please wait for an admin to approve your account.";
			try {
				emailService.sendSimpleMessage(u.getEmail(), "Registration", message);
			} catch (Exception e) {
				e.printStackTrace();
			}
			re = ResponseEntity.ok(message);
		} catch (Exception e) {
			message = "Oops! Something went wrong.";
			System.out.println(e.getMessage());
			re = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}

		return re;
	}

	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public ResponseEntity<?> me(HttpServletRequest request) throws Exception {
		Optional<User> optionalUser = null;
		try {

			final String authorizationHeader = request.getHeader("Authorization");

			String email = null;
			String jwt = null;

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				email = jwtUtil.extractEmail(jwt);
			}

			optionalUser = userRepo.findByEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (optionalUser.isPresent()) {
			User u = optionalUser.get();
			UserDTO user = new UserDTO(u.getIdUser(), u.getIme(), u.getPrezime(), u.getEmail(), u.getRole().getNaziv());
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Oops! Something went wrong.");
		}
	}
}
