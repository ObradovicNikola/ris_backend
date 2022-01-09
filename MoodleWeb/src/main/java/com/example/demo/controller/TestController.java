package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.email.EmailServiceImpl;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.dto.UserDTO;

import model.User;

@RestController
//@RequestMapping(value = "/test")
public class TestController {

	@Autowired
	UserRepository ur;

	@Autowired
	EmailServiceImpl emailService;

	@GetMapping(value = "/")
	public String greeting() {
		emailService.sendSimpleMessage("obradovicnikola009@gmail.com", "Subject - test", "This is message text");
		return "helou";
	}

	@GetMapping(value = "/user")
	public String user() {
		return "this is user page";
	}

	@GetMapping(value = "/users")
	public List<UserDTO> users() {
		List<User> all = ur.findAll();
		List<UserDTO> users = new ArrayList<UserDTO>();

		for (User u : all) {
			UserDTO user = new UserDTO(u.getIdUser(), u.getIme(), u.getPrezime(), u.getEmail(), u.getRole().getNaziv());
			users.add(user);
		}

		return users;
	}

	@GetMapping(value = "/admin")
	public String admin() {
		return "only admin can see this";
	}
}
