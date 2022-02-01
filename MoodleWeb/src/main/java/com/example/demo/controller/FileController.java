package com.example.demo.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.payload.UploadFileResponse;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.MaterijalRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.dto.Message;
import com.example.demo.security.JWTUtil;
import com.example.demo.service.FileStorageService;

import model.Course;
import model.Materijal;
import model.Pohadja;
import model.User;

@RestController
public class FileController {

	@Autowired
	CourseRepository courseRepo;

	@Autowired
	MaterijalRepository materijalRepo;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private FileStorageService fileStorageService;

	@PostMapping("/uploadFile")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("idCourse") Integer idCourse, HttpServletRequest request) {
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

			String fileName = fileStorageService.storeFile(file, idCourse);

			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/downloadFile/" + idCourse.toString() + "/").path(fileName).toUriString();

			Materijal mat = new Materijal();
			mat.setCourse(c);
			mat.setNaslov(fileName);
			mat.setPutanja(fileDownloadUri);
			mat.setContentType(file.getContentType());
			materijalRepo.save(mat);

			return ResponseEntity
					.ok(new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		message.setMessage("Oops! Something went wrong.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

//	@PostMapping("/uploadMultipleFiles")
//	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
//			@RequestParam("file") Integer idCourse) {
//		return Arrays.asList(files).stream().map(file -> uploadFile(file, idCourse)).collect(Collectors.toList());
//	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			System.out.println("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@GetMapping("/downloadFile/{idCourse}/{fileName:.+}")
	public ResponseEntity<?> downloadFile(@PathVariable String fileName, @PathVariable Integer idCourse,
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
			if (optionalUser.isPresent()) {
				User u = optionalUser.get();

				Course c = courseRepo.findById(idCourse).get();

				// ako je profesor ulogovan, proveri da li on predaje na kursu
				if (u.getRole().getNaziv().equals("PROFESOR")) {
					if (c.getProfesor().getIdUser() != u.getIdUser()) {
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
						// ako student nije upisan, ne moze da skida materijal
						message.setMessage("Access denied.");
						return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
					}
				}
			}

			// Load file as Resource
			Resource resource = fileStorageService.loadFileAsResource(fileName, idCourse);

			// Try to determine file's content type
			String contentType = null;
			try {
				contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			} catch (IOException ex) {
				System.out.println("Could not determine file type.");
			}

			// Fallback to the default content type if type could not be determined
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		} catch (Exception e) {
			e.printStackTrace();
		}

		message.setMessage("Oops! Something went wrong.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	@PostMapping("/deleteFile/{idCourse}/{fileName:.+}")
	public ResponseEntity<?> deleteFile(@PathVariable String fileName, @PathVariable Integer idCourse,
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
			if (optionalUser.isPresent()) {
				User u = optionalUser.get();

				Course c = courseRepo.findById(idCourse).get();

				// ako je profesor ulogovan, proveri da li on predaje na kursu
				if (u.getRole().getNaziv().equals("PROFESOR")) {
					if (c.getProfesor().getIdUser() != u.getIdUser()) {
						message.setMessage("Access denied.");
						return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
					}
				}
			}

			// Load file as Resource
			fileStorageService.deleteFile(fileName, idCourse);

			materijalRepo.deleteByNaslovAndIdCourse(fileName, idCourse);

			message.setMessage("Resource deleted succesfully.");
			return ResponseEntity.ok().body(message);
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage(e.getMessage());
		}

//		message.setMessage("Oops! Something went wrong.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
}