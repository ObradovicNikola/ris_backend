package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import model.Aktivnost;
import model.Course;
import model.Obavestenje;
import model.User;

@RestController
public class ActivitiesController {

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

//	@Autowired
//	private PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	EmailServiceImpl emailService;

	@PostMapping("/course/{idCourse}/activity")
	public ResponseEntity<?> createNewActivity(@PathVariable Integer idCourse, @RequestParam String naziv,
			@RequestParam Integer datumAktivnostiunixTimestamp, @RequestParam(required = false) String opis,
			@RequestParam Integer maxOcena, HttpServletRequest request) {
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

			Aktivnost a = new Aktivnost();
			a.setCourse(c);
			a.setMaxOcena(maxOcena);
			a.setNaziv(naziv);
			a.setDatum(datumAktivnostiunixTimestamp);
			if (opis != null && !opis.isBlank()) {
				a.setOpis(opis);
			}
			aktivnostRepo.save(a);

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
			Date date = new Date((long) datumAktivnostiunixTimestamp * 1000);
			String sadrzaj = "Aktivnost " + naziv + " je kreirana za predmet " + c.getNaziv() + ". Datum: "
					+ dateFormat.format(date);
			Obavestenje o = new Obavestenje();
			o.setCourse(c);
			o.setSadrzaj(sadrzaj);
			int unixTimestamp = (int) Instant.now().getEpochSecond();
			o.setDatum(unixTimestamp);
			obavestenjeRepo.save(o);

			// get list of all students, send them emails
			List<User> studenti = c.getPohadjas().stream().map((x) -> {
				return x.getStudent().getUser();
			}).collect(Collectors.toList());

			for (User s : studenti) {
				try {
					emailService.sendSimpleMessage(s.getEmail(), c.getNaziv() + " - " + naziv, o.getSadrzaj());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			message.setMessage("Activity created successfully");

			return ResponseEntity.ok(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		message.setMessage("Oops! Something went wrong.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	@DeleteMapping("/course/activity/{idAktivnost}")
	public ResponseEntity<?> deleteActivity(@PathVariable Integer idAktivnost, HttpServletRequest request) {
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
			Course c = aktivnostRepo.getById(idAktivnost).getCourse();
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

			aktivnostRepo.deleteById(idAktivnost);

			message.setMessage("Activity deleted successfully");

			return ResponseEntity.ok(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		message.setMessage("Oops! Something went wrong.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
}
