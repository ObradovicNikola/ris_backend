package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.dto.CourseDTO;
import com.example.demo.repository.dto.MaterialDTO;
import com.example.demo.repository.dto.Message;
import com.example.demo.repository.dto.UserDTO;
import com.example.demo.security.JWTUtil;
import com.example.demo.security.models.AuthenticationRequest;

import model.Course;
import model.Materijal;
import model.Pohadja;
import model.User;

@RestController
public class CoursesController {

	@Autowired
	CourseRepository courseRepo;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserRepository userRepo;

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

				// provera da li ulogovani korisnik ima dozvolu da gleda kurs
				if (u.getRole().getNaziv().equals("PROFESOR")) {
					if (u.getIdUser() != c.getProfesor().getIdUser()) {
						message.setMessage("Access denied.");
						return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
					}
				} else if (u.getRole().getNaziv().equals("STUDENT")) {
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
//						TODO: ako student nije upisan, vrati mu informacije o kursu da moze da se upise ako zna sifru
						message.setMessage("Access denied.");
						return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
					}
				}

				User uProf = c.getProfesor().getUser();
				UserDTO prof = new UserDTO(uProf.getIdUser(), uProf.getIme(), uProf.getPrezime(), uProf.getEmail(),
						uProf.getRole().getNaziv());

				CourseDTO course = new CourseDTO(c.getIdCourse(), c.getNaziv(), c.getOpis(), c.getSadrzaj(), prof);
				// TODO: staviti materijale kursa ako je ulogovani korisnik prijavljen na kurs
				// bilo kao profesor bilo kao student
				// TODO: dodati atribut boolean, da li je student prijavljen na kurs, ako
				// student poziva metod
				// TODO: testirati nakon sto profesor moze da doda materijale na kurs

				List<MaterialDTO> materials = new ArrayList<>();
				List<Materijal> materijali = c.getMaterijals();
				for (Materijal m : materijali) {
					MaterialDTO mat = new MaterialDTO(m.getIdMaterijal(), m.getNaslov(), m.getPutanja(),
							m.getContentType());
					materials.add(mat);
				}
				course.setMaterials(materials);

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

	// TODO: svi kursevi ulogovanog korisnika, bio on student ili profesor
	// myCourses()
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

	// TODO: upisivanje na kurs, moguce samo ako je ulogovani korisnik student

	// TODO: dodavanje materijala na kurs, samo profesor moze
}
