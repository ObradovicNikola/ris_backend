package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.repository.BibliotekaKorisnikRepository;
import com.example.demo.repository.BibliotekaUlogaRepository;
import com.example.demo.repository.KnjigaRepository;

import model.BibliotekaKorisnik;
import model.BibliotekaUloga;
import model.Knjiga;


@Controller
@ControllerAdvice
@RequestMapping(value = "auth")
public class LoginControlor {
	@Autowired
	BibliotekaUlogaRepository r;
	@Autowired
	BibliotekaKorisnikRepository ur;
	
	@Autowired
	KnjigaRepository kr;
	
	@ModelAttribute
	public void getRoles(Model model) {
		List<BibliotekaUloga> roles=r.findAll();
		model.addAttribute("roles", roles);
		
	}
	
	   @RequestMapping(value = "/loginPage", method = RequestMethod.GET)
	    public String loginPage() {    
	    	return "login";
	     
	    }
	   
	   @RequestMapping(value = "/login", method = RequestMethod.POST)
	    public String loginHello() {    
	    	return "hello";
	     
	    }
	   
	    @RequestMapping(value = "/access_denied", method = RequestMethod.GET)
	    public String deniedPage() {    
	          return "access_denied";
	     
	    }
	    @RequestMapping(value = "registerUser", method = RequestMethod.GET)
		public String newUser(Model model) {
			BibliotekaKorisnik u = new BibliotekaKorisnik();
			model.addAttribute("user", u);
			return "register";
		}
	 
	   @RequestMapping(value = "register", method = RequestMethod.POST)
		public String saveUser(@ModelAttribute("user") BibliotekaKorisnik u) {
	    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	     	u.setSifra(passwordEncoder.encode(u.getSifra()));
			
			for (BibliotekaUloga r : u.getUlogas()) {
				r.addKorisnik(u);
				
			}
	    	ur.save(u);
			return "login";

		}
	    
	    @RequestMapping(value="/logout", method = RequestMethod.GET)
	    public String logoutPage (HttpServletRequest request, HttpServletResponse response){
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null){    
	            SecurityContextHolder.getContext().setAuthentication(null);
	        }
	        return "redirect:/auth/loginPage";
	    }
	    
	    @RequestMapping(value="/pocetna", method = RequestMethod.GET)
	    public String getPocetna (){
	       
	        return "pocetna";
	    }
	    
	    @RequestMapping(value="/getSve", method=RequestMethod.GET)
		public String getSve(HttpServletRequest request) {
			List<Knjiga> knjige = kr.findAll();
			request.getSession().setAttribute("knjige", knjige);
			return "sveKnjige";
		}

	    
}
