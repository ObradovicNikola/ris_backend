package com.example.demo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.payload.NewCourseRequest;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.OceneRepository;
import com.example.demo.repository.ProfesorRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.dto.AktivnostDTO;
import com.example.demo.repository.dto.Message;
import com.example.demo.repository.dto.UserDTO;
import com.example.demo.security.models.AdminRegistrationRequest;

import model.Aktivnost;
import model.Course;
import model.Ocene;
import model.Profesor;
import model.User;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
public class AdminController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	ProfesorRepository profesorRepo;

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	CourseRepository courseRepo;

	@Autowired
	OceneRepository oceneRepo;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/admin/all")
	public ResponseEntity<?> getAllAccounts(HttpServletRequest request) {
		List<User> allUsers = userRepo.findAll();
		List<UserDTO> allUsersDTO = new ArrayList<>();
		for (User user : allUsers) {
			if (!user.getRole().getNaziv().equals("ADMIN")) {
				UserDTO u = new UserDTO();
				u.setEmail(user.getEmail());
				u.setIme(user.getIme());
				u.setIdUser(user.getIdUser());
				u.setPrezime(user.getPrezime());
				u.setRole(user.getRole().getNaziv());
				u.setEnabled(user.getEnabled());

				allUsersDTO.add(u);
			}
		}

		return ResponseEntity.ok(allUsersDTO);
	}

	@GetMapping("/admin/disabled")
	public ResponseEntity<?> getDisabledAccounts(HttpServletRequest request) {
		List<User> disabledUsers = userRepo.getAllDisabled();
		List<UserDTO> disabledUsersDTO = new ArrayList<>();
		for (User user : disabledUsers) {
			if (!user.getRole().getNaziv().equals("ADMIN")) {
				UserDTO u = new UserDTO();
				u.setEmail(user.getEmail());
				u.setIme(user.getIme());
				u.setIdUser(user.getIdUser());
				u.setPrezime(user.getPrezime());
				u.setRole(user.getRole().getNaziv());

				disabledUsersDTO.add(u);
			}
		}

		return ResponseEntity.ok(disabledUsersDTO);
	}

	@PostMapping("/admin/enable/{idUser}")
	public ResponseEntity<?> enableAccount(@PathVariable Integer idUser, HttpServletRequest request) {

		Message message = new Message();

		try {
			User u = userRepo.getById(idUser);
			if (!u.getRole().getNaziv().equals("ADMIN")) {
				u.setEnabled(true);
				userRepo.save(u);
				message.setMessage("User susuccessfully enabled.");
			} else {
				message.setMessage("Access denied.");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage("Oops! Something went wrong.");
		}
		return ResponseEntity.ok(message);
	}

	// disabling account, can't disable other admins
	@PostMapping("/admin/disable/{idUser}")
	public ResponseEntity<?> disableAccount(@PathVariable Integer idUser, HttpServletRequest request) {

		Message message = new Message();

		try {
			User u = userRepo.getById(idUser);
			if (!u.getRole().getNaziv().equals("ADMIN")) {
				u.setEnabled(false);
				userRepo.save(u);
				message.setMessage("User susuccessfully disabled.");
			} else {
				message.setMessage("Access denied.");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage("Oops! Something went wrong.");
		}
		return ResponseEntity.ok(message);
	}

	@PostMapping(value = "/admin/newuser")
	public ResponseEntity<?> registerUser(@RequestBody AdminRegistrationRequest registrationRequest) throws Exception {

		Message message = new Message();

		if (userRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
			message.setMessage("Email is already registered...");
			return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(message);
		}

		// ne dozvoli registraciju nezeljenih uloga
		if (registrationRequest.getIdRole() != 2 && registrationRequest.getIdRole() != 3) {
			message.setMessage("Undefined role...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}

		User u = new User();
		u.setEmail(registrationRequest.getEmail());
		u.setPassword(bCryptPasswordEncoder.encode(registrationRequest.getPassword()));
		u.setIme(registrationRequest.getIme());
		u.setPrezime(registrationRequest.getPrezime());
		u.setRole(roleRepo.findById(registrationRequest.getIdRole()).get());
		u.setEnabled(registrationRequest.isEnabled());

		try {
			userRepo.save(u);
			message.setMessage("New account created susuccessfully");
			return ResponseEntity.ok(message);
		} catch (Exception e) {
			message.setMessage("Oops! Something went wrong.");
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	@PostMapping(value = "/admin/newcourse")
	public ResponseEntity<?> createNewCourse(@RequestBody NewCourseRequest courseRequest) throws Exception {

		Message message = new Message();
		try {
			Profesor p = profesorRepo.findById(courseRequest.getIdProfesor()).get();

			Course course = new Course();
			course.setNaziv(courseRequest.getNaziv());
			course.setOpis(courseRequest.getOpis());
			course.setSadrzaj(courseRequest.getSadrzaj());
			course.setSifra(bCryptPasswordEncoder.encode(courseRequest.getSifra()));
			course.setProfesor(p);

			courseRepo.save(course);

			message.setMessage("Course saved susuccessfully");
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage("Can't find the professor...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
	}

	@GetMapping(value = "/admin/profesors")
	public ResponseEntity<?> getAllProfesors() {
		List<Profesor> profesors = profesorRepo.findAll();
		List<UserDTO> profesorsDTO = new ArrayList<>();
		for (Profesor p : profesors) {
			User user = p.getUser();
			// mozemo postaviti profesora sada, a omoguciti mu login kasnije
//			if (user.getEnabled()) {
			UserDTO u = new UserDTO();
			u.setEmail(user.getEmail());
			u.setIme(user.getIme());
			u.setIdUser(user.getIdUser());
			u.setPrezime(user.getPrezime());
			u.setRole(user.getRole().getNaziv());

			profesorsDTO.add(u);
//			}
		}

		return ResponseEntity.ok(profesorsDTO);
	}

	@GetMapping("/admin/getuser/{idUser}")
	public ResponseEntity<?> getUser(@PathVariable Integer idUser, HttpServletRequest request) {
		Message message = new Message();
		Optional<User> optionalU = userRepo.findById(idUser);

		if (optionalU.isPresent()) {
			User user = optionalU.get();

			if (!user.getRole().getNaziv().equals("ADMIN")) {

				UserDTO u = new UserDTO();
				u.setEmail(user.getEmail());
				u.setIme(user.getIme());
				u.setIdUser(user.getIdUser());
				u.setPrezime(user.getPrezime());
				u.setRole(user.getRole().getNaziv());
				u.setEnabled(user.getEnabled());

				return ResponseEntity.ok(u);
			} else {
				message.setMessage("This incident will be reported.");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
			}
		} else {
			message.setMessage("Can't find the user...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
	}

	@GetMapping("/admin/getuser/{idUser}/aktivnosti")
	public ResponseEntity<?> getUserAktivnosti(@PathVariable Integer idUser, HttpServletRequest request) {
		Optional<User> optionalUser = null;
		Message message = new Message();
		try {
			optionalUser = userRepo.findById(idUser);
			if (!optionalUser.isPresent()) {
				message.setMessage("Can't find the user...");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
			}

			User u = optionalUser.get();
			if (u.getRole().getNaziv().equals("ADMIN")) {
				message.setMessage("This incident will be reported.");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
			}
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

	// izvestaj o svim profesorima
	@GetMapping("/admin/izvestaj/profesori")
	public void getIzvestajProfesori(HttpServletResponse response) throws JRException, IOException {
		String pattern = "dd-MMM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String date = simpleDateFormat.format(new Date());

		List<User> profesors = profesorRepo.findAll().stream().map((x) -> {
			return x.getUser();
		}).collect(Collectors.toList());
		JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(profesors);

		InputStream employeeReportStream = getClass().getResourceAsStream("/jasperreports/profesoriReport.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("date", date);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, data);

		// forces download
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=profesori.pdf");
		response.setHeader(headerKey, headerValue);

		ServletOutputStream out = response.getOutputStream();

		JasperExportManager.exportReportToPdfStream(jasperPrint, out);
	}

	// izvestaj o jednom kursu sa svim polaznicima
	@GetMapping("/admin/izvestaj/kurs/{idCourse}")
	public void getIzvestajKurs(@PathVariable Integer idCourse, HttpServletResponse response) {
		try {
			String pattern = "dd-MMM-yyyy";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

			String date = simpleDateFormat.format(new Date());

			Course course = courseRepo.findById(idCourse).get();
			User prof = course.getProfesor().getUser();

			List<User> students = course.getPohadjas().stream().map((x) -> {
				return x.getStudent().getUser();
			}).collect(Collectors.toList());
			JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(students);

			InputStream employeeReportStream = getClass().getResourceAsStream("/jasperreports/kursReport.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("date", date);
			parameters.put("naziv", course.getNaziv());
			parameters.put("opis", course.getOpis());
			parameters.put("profesorIme", prof.getIme());
			parameters.put("profesorPrezime", prof.getPrezime());

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, data);

			// forces download
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=profesori.pdf");
			response.setHeader(headerKey, headerValue);

			ServletOutputStream out = response.getOutputStream();

			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
