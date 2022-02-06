package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.email.EmailServiceImpl;
import com.example.demo.payload.GetOceneResponse;
import com.example.demo.payload.SaveOceneRequest;
import com.example.demo.repository.AktivnostRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.OceneRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.dto.AktivnostDTO;
import com.example.demo.repository.dto.Message;
import com.example.demo.security.JWTUtil;

import model.Aktivnost;
import model.Course;
import model.Ocene;
import model.Student;
import model.User;

@RestController
public class OceneController {

	@Autowired
	CourseRepository courseRepo;

	@Autowired
	StudentRepository studentRepo;

	@Autowired
	AktivnostRepository aktivnostRepo;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private OceneRepository oceneRepo;

	@Autowired
	EmailServiceImpl emailService;

	@PostMapping("/course/ocene/{idAktivnost}")
	public ResponseEntity<?> sacuvajOcene(@PathVariable Integer idAktivnost,
			@RequestBody List<SaveOceneRequest> oceneRequest, HttpServletRequest request) {

		Optional<User> optionalUser = null;
		Message message = new Message();
		Aktivnost akt = aktivnostRepo.getById(idAktivnost);
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
			Course c = akt.getCourse();
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

			for (SaveOceneRequest ocenaRequest : oceneRequest) {
				// ako je ocena veca od maxOcena, nece biti upisana
				if (akt.getMaxOcena() >= ocenaRequest.getOcena()) {
					// mysql tabela se zove Ocene
					Ocene ocena;
					Optional<Ocene> optionalOcena = oceneRepo.findByStudentAndAktivnost(ocenaRequest.getIdStudent(),
							idAktivnost);
					if (optionalOcena.isPresent()) {
						ocena = optionalOcena.get();
					} else {
						ocena = new Ocene();
						Student stud = studentRepo.getById(ocenaRequest.getIdStudent());
						ocena.setAktivnost(akt);
						ocena.setStudent(stud);
					}
					ocena.setOcena(ocenaRequest.getOcena());

					oceneRepo.save(ocena);
				}
			}

			String emailPoruka = "Ocene su uspešno sačuvane.";
			try {
				emailService.sendSimpleMessage(c.getProfesor().getUser().getEmail(), c.getNaziv() + " - ocene",
						emailPoruka);
			} catch (Exception e) {
				e.printStackTrace();
			}

			message.setMessage("Grades saved successfully");

			return ResponseEntity.ok(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		message.setMessage("Oops! Something went wrong.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	@GetMapping("/course/ocene/{idAktivnost}")
	public ResponseEntity<?> getOcenePoAktivnosti(@PathVariable Integer idAktivnost, HttpServletRequest request) {
		Optional<User> optionalUser = null;
		Message message = new Message();
		Aktivnost akt = aktivnostRepo.getById(idAktivnost);
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
			Course c = akt.getCourse();
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

			List<GetOceneResponse> oceneResponse = new ArrayList<>();

			List<User> studenti = akt.getCourse().getPohadjas().stream().map((x) -> {
				return x.getStudent().getUser();
			}).collect(Collectors.toList());

			for (User student : studenti) {
				GetOceneResponse or = new GetOceneResponse();

				or.setIdStudent(student.getIdUser());
				or.setIme(student.getIme());
				or.setPrezime(student.getPrezime());
				Optional<Ocene> ocena = oceneRepo.findByStudentAndAktivnost(student.getIdUser(), idAktivnost);
				if (ocena.isPresent()) {
					or.setOcena(ocena.get().getOcena());
				} else {
					or.setOcena(0);
				}

				oceneResponse.add(or);
			}

			return ResponseEntity.ok(oceneResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		message.setMessage("Oops! Something went wrong.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	// get all activities for user id
	// 1. kada je user ulogovan trazi njegove aktivnosti
	@GetMapping("/courses/aktivnosti")
	public ResponseEntity<?> getAktivnostiByIdUser(HttpServletRequest request) {
		Optional<User> optionalUser = null;
		Message message = new Message();
		try {

			final String authorizationHeader = request.getHeader("Authorization");

			String email = null;
			String jwt = null;

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				email = jwtUtil.extractEmail(jwt);
			}

			optionalUser = userRepo.findByEmail(email);
			if (!optionalUser.isPresent()) {
				message.setMessage("Not authenticated.");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
			}

			User u = optionalUser.get();
			List<AktivnostDTO> aktivnostiResponse = new ArrayList<>();
			List<Aktivnost> sveAktivnosti = null;
			if (u.getRole().getNaziv().equals("STUDENT")) {
				sveAktivnosti = u.getStudent().getPohadjas().stream().flatMap((x) -> {
					return x.getCourse().getAktivnosts().stream();
				}).collect(Collectors.toList());
			} else {
				sveAktivnosti = u.getProfesor().getCourses().stream().flatMap((x) -> {
					return x.getAktivnosts().stream();
				}).collect(Collectors.toList());
			}

			if (sveAktivnosti != null) {
				for (Aktivnost aktivnost : sveAktivnosti) {
					AktivnostDTO aktDTO = new AktivnostDTO();
					aktDTO.setNaziv(aktivnost.getNaziv());
					aktDTO.setOpis(aktivnost.getOpis());
					aktDTO.setIdAktivnost(aktivnost.getIdAktivnost());
					aktDTO.setMaxOcena(aktivnost.getMaxOcena());
					aktDTO.setDatum(aktivnost.getDatum());
					aktDTO.setNazivKursa(aktivnost.getCourse().getNaziv());
					if (u.getRole().getNaziv().equals("STUDENT")) {
						Optional<Ocene> ocena = oceneRepo.findByStudentAndAktivnost(u.getIdUser(),
								aktivnost.getIdAktivnost());
						if (ocena.isPresent()) {
							aktDTO.setOcena(ocena.get().getOcena());
						} else {
							aktDTO.setOcena(-1);
						}
					}
					aktivnostiResponse.add(aktDTO);
				}
			}

			return ResponseEntity.ok(aktivnostiResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

		message.setMessage("Oops! Something went wrong.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

// get all activities for user id
// 2. kada je admin ulogovan moze da trazi aktivnosti za prosledjen parametar
// idUser
//	@GetMapping("/courses/aktivnosti/{idUser}")
}
