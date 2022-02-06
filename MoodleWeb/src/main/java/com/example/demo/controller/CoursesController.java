package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.email.EmailServiceImpl;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.OceneRepository;
import com.example.demo.repository.PohadjaRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.dto.AktivnostDTO;
import com.example.demo.repository.dto.CourseDTO;
import com.example.demo.repository.dto.MaterialDTO;
import com.example.demo.repository.dto.Message;
import com.example.demo.repository.dto.ObavestenjeDTO;
import com.example.demo.repository.dto.UserDTO;
import com.example.demo.security.JWTUtil;

import model.Aktivnost;
import model.Course;
import model.Materijal;
import model.Obavestenje;
import model.Ocene;
import model.Pohadja;
import model.Student;
import model.User;

@RestController
public class CoursesController {

	@Autowired
	CourseRepository courseRepo;

	@Autowired
	PohadjaRepository pohadjaRepo;

	@Autowired
	StudentRepository studentRepo;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private OceneRepository oceneRepo;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	EmailServiceImpl emailService;

	@RequestMapping(value = "/courses", method = RequestMethod.GET)
	public ResponseEntity<?> getAllCourses() {
		List<Course> kursevi = courseRepo.findAll();
		List<CourseDTO> courses = new ArrayList<>();
		for (Course c : kursevi) {
			User uProf = c.getProfesor().getUser();
			UserDTO prof = new UserDTO(uProf.getIdUser(), uProf.getIme(), uProf.getPrezime(), uProf.getEmail(),
					uProf.getRole().getNaziv());
			CourseDTO cDTO = new CourseDTO(c.getIdCourse(), c.getNaziv(), c.getOpis(), c.getSadrzaj(), prof);

			courses.add(cDTO);
		}

		return ResponseEntity.ok(courses);
	}

	@RequestMapping(value = "/courses/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCourseById(@PathVariable String id, HttpServletRequest request) {
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
			if (optionalUser.isPresent()) {
				User u = optionalUser.get();
				Course c = courseRepo.findById(Integer.parseInt(id)).get();
				boolean upisan = false;

				// provera da li ulogovani korisnik ima dozvolu da gleda kurs
				if (u.getRole().getNaziv().equals("PROFESOR")) {
					if (u.getIdUser() != c.getProfesor().getIdUser()) {
						message.setMessage("Access denied.");
						return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
					}
				} else if (u.getRole().getNaziv().equals("STUDENT")) {
					int i = 0;
					List<Pohadja> pohadjas = c.getPohadjas();
					while (i < pohadjas.size() && upisan == false) {
						if (pohadjas.get(i).getStudent().getIdUser() == u.getIdUser()) {
							upisan = true;
						}
						i++;
					}
				}

				User uProf = c.getProfesor().getUser();
				UserDTO prof = new UserDTO(uProf.getIdUser(), uProf.getIme(), uProf.getPrezime(), uProf.getEmail(),
						uProf.getRole().getNaziv());

				CourseDTO course = new CourseDTO(c.getIdCourse(), c.getNaziv(), c.getOpis(), c.getSadrzaj(), prof);
				course.setUpisan(upisan);

				if (u.getRole().getNaziv().equals("STUDENT") && !upisan)
					return ResponseEntity.ok(course);

				List<MaterialDTO> materials = new ArrayList<>();
				List<Materijal> materijali = c.getMaterijals();
				for (Materijal m : materijali) {
					MaterialDTO mat = new MaterialDTO(m.getIdMaterijal(), m.getNaslov(), m.getPutanja(),
							m.getContentType());
					materials.add(mat);
				}
				course.setMaterials(materials);

				List<ObavestenjeDTO> notifications = new ArrayList<>();
				List<Obavestenje> obavestenja = c.getObavestenjes();
				for (Obavestenje o : obavestenja) {
					ObavestenjeDTO ob = new ObavestenjeDTO(o.getIdObavestenje(), o.getDatum(), o.getSadrzaj(),
							c.getIdCourse());
					notifications.add(ob);
				}
				course.setNotifications(notifications);

				List<AktivnostDTO> activities = new ArrayList<>();
				List<Aktivnost> aktivnosti = c.getAktivnosts();
				for (Aktivnost a : aktivnosti) {
					AktivnostDTO akt = new AktivnostDTO(a.getNaziv(), a.getOpis(), a.getIdAktivnost(), a.getMaxOcena(),
							a.getDatum());
					if (u.getRole().getNaziv().equals("STUDENT")) {
						Optional<Ocene> ocena = oceneRepo.findByStudentAndAktivnost(u.getIdUser(), a.getIdAktivnost());
						if (ocena.isPresent()) {
							akt.setOcena(ocena.get().getOcena());
						}
					}
					activities.add(akt);
				}
				course.setActivities(activities);

				return ResponseEntity.ok(course);
			} else {
				message.setMessage("You are not logged in.");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		message.setMessage("Oops! Something went wrong.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	@RequestMapping(value = "/mycourses", method = RequestMethod.GET)
	public ResponseEntity<?> getMyCourses(HttpServletRequest request) {
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
			if (optionalUser.isPresent()) {
				User u = optionalUser.get();
//				UserDTO user = new UserDTO(u.getIdUser(), u.getIme(), u.getPrezime(), u.getEmail(),
//						u.getRole().getNaziv());

				List<Course> kursevi;
				if (u.getRole().getNaziv().equals("STUDENT")) {
					kursevi = courseRepo.findMyCoursesAsAStudent(u.getIdUser());
				} else if (u.getRole().getNaziv().equals("PROFESOR")) {
					kursevi = courseRepo.findMyCoursesAsAProfesor(u.getIdUser());
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Oops! Something went wrong.");
				}

				List<CourseDTO> courses = new ArrayList<>();
				for (Course c : kursevi) {
					User uProf = c.getProfesor().getUser();
					UserDTO prof = new UserDTO(uProf.getIdUser(), uProf.getIme(), uProf.getPrezime(), uProf.getEmail(),
							uProf.getRole().getNaziv());
					CourseDTO cDTO = new CourseDTO(c.getIdCourse(), c.getNaziv(), c.getOpis(), c.getSadrzaj(), prof);

					courses.add(cDTO);
				}

				return ResponseEntity.ok(courses);
			} else {
				throw new Exception("Can't authenticate the user.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Oops! Something went wrong.");
		}
	}

	@RequestMapping(value = "/courses/signup", method = RequestMethod.POST)
	public ResponseEntity<?> courseSignup(@RequestParam Integer idCourse, @RequestParam String sifra,
			HttpServletRequest request) {
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
			if (optionalUser.isPresent()) {
				User u = optionalUser.get();
				Course c = courseRepo.findById(idCourse).get();

				// provera da li je student vec upisan na kurs
				if (u.getRole().getNaziv().equals("STUDENT")) {
					boolean upisan = false;
					int i = 0;
					List<Pohadja> pohadjas = c.getPohadjas();
					while (i < pohadjas.size() && upisan == false) {
						if (pohadjas.get(i).getStudent().getIdUser() == u.getIdUser()) {
							upisan = true;
						}
						i++;
					}
					if (upisan) {
						message.setMessage("Already signed up.");
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
					}
				}

				if (!bCryptPasswordEncoder.matches(sifra, c.getSifra())) {
					message.setMessage("Bad password.");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
				}

				Student s = studentRepo.findById(u.getIdUser()).get();
				Pohadja pohadja = new Pohadja();
				pohadja.setCourse(c);
				pohadja.setStudent(s);
				pohadjaRepo.save(pohadja);

				String emailPoruka = "Upisani ste na kurs " + c.getNaziv();
				try {
					emailService.sendSimpleMessage(u.getEmail(), c.getNaziv() + " - upis", emailPoruka);
				} catch (Exception e) {
					e.printStackTrace();
				}

				message.setMessage("Successfully signed up.");
				return ResponseEntity.ok(message);
			} else {
				message.setMessage("You are not logged in.");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		message.setMessage("Oops! Something went wrong.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	@RequestMapping(value = "/courses/signout", method = RequestMethod.POST)
	public ResponseEntity<?> courseSignout(@RequestParam Integer idCourse, HttpServletRequest request) {
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
			if (optionalUser.isPresent()) {
				User u = optionalUser.get();
				Course c = courseRepo.findById(idCourse).get();

				// provera da li je student vec upisan na kurs
				if (u.getRole().getNaziv().equals("STUDENT")) {
					boolean upisan = false;
					int i = 0;
					List<Pohadja> pohadjas = c.getPohadjas();
					while (i < pohadjas.size() && upisan == false) {
						if (pohadjas.get(i).getStudent().getIdUser() == u.getIdUser()) {
							upisan = true;
						}
						i++;
					}
					if (!upisan) {
						message.setMessage("You are not signed in to this course.");
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
					}
				}

				pohadjaRepo.deleteByCourseAndStudent(idCourse, u.getIdUser());

				String emailPoruka = "Ispisani ste sa kursa " + c.getNaziv();
				try {
					emailService.sendSimpleMessage(u.getEmail(), c.getNaziv() + " - ispis", emailPoruka);
				} catch (Exception e) {
					e.printStackTrace();
				}

				message.setMessage("Successfully signed out.");
				return ResponseEntity.ok(message);
			} else {
				message.setMessage("You are not logged in.");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		message.setMessage("Oops! Something went wrong.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

}
