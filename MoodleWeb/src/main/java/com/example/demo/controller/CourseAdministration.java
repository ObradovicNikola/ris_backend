package com.example.demo.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.email.EmailServiceImpl;
import com.example.demo.repository.AktivnostRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.ObavestenjeRepository;
import com.example.demo.repository.PohadjaRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.dto.Message;
import com.example.demo.security.JWTUtil;

import model.Course;
import model.User;

@RestController
public class CourseAdministration {
	@Autowired
	CourseRepository courseRepo;

	@Autowired
	PohadjaRepository pohadjaRepo;

//	@Autowired
//	StudentRepository studentRepo;

	@Autowired
	ObavestenjeRepository obavestenjeRepo;

	@Autowired
	AktivnostRepository aktivnostRepo;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	EmailServiceImpl emailService;

	@PostMapping("/course/{idCourse}/newpassword")
	public ResponseEntity<?> createNewActivity(@PathVariable Integer idCourse, @RequestParam String password,
			HttpServletRequest request) {
		Optional<User> optionalUser = null;
		Message message = new Message();
		try {

			// provera da li profesor ima dozvolu...
			final String authorizationHeader = request.getHeader("Authorization");

			String email = null;
			String jwt = null;

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				email = jwtUtil.extractEmail(jwt);
			}

			optionalUser = userRepo.findByEmail(email);
			Course c = courseRepo.findById(idCourse).get();
			if (optionalUser.isPresent()) {
				User u = optionalUser.get();

				// ako je profesor ulogovan, proveri da li on predaje na kursu
				if (u.getRole().getNaziv().equals("PROFESOR")) {
					if (c.getProfesor().getIdUser() != u.getIdUser()) {
						message.setMessage("Access denied.");
						return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
					}
				}
			}

			String encodedPass = bCryptPasswordEncoder.encode(password);
			c.setSifra(encodedPass);
			courseRepo.save(c);

			String emailPoruka = "Sifra je uspesno promenjena.";
			try {
				emailService.sendSimpleMessage(c.getProfesor().getUser().getEmail(), c.getNaziv() + " - sifra",
						emailPoruka);
			} catch (Exception e) {
				e.printStackTrace();
			}

			message.setMessage("Password updated successfully");

			return ResponseEntity.ok(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		message.setMessage("Oops! Something went wrong.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
}
