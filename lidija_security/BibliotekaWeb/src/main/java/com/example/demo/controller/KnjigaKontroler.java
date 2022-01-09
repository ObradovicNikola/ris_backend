package com.example.demo.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.repository.KnjigaRepository;
import com.example.demo.repository.PrimerakRepository;
import com.example.demo.repository.ZaduzenjeRepository;

import model.Knjiga;
import model.Primerak;


@Controller
@RequestMapping(value="/knjige")
//@Validated //annotation to the controller at class level to tell Spring to evaluate the constraint annotations on method parameters
public class KnjigaKontroler {

	@Autowired
	KnjigaRepository kr;
	
	@Autowired
	PrimerakRepository pr;
	
	@Autowired
	ZaduzenjeRepository zr;
	
	@RequestMapping(value="/saveKnjiga", method=RequestMethod.POST)
	public String saveKnjiga(Model m, HttpServletRequest request, Knjiga k) {	
		kr.save(k);
		request.getSession().setAttribute("knjiga", k);
		m.addAttribute("poruka", "Knjiga je uspesno sacuvana.");
		System.out.println("Zastoooo_");
		return "unos/UnosPrimeraka";
	}
	
	@RequestMapping(value="/savePrimerci", method=RequestMethod.POST)
	public String savePrimerci(Integer brPrimeraka, HttpServletRequest request, Model m) {
		Knjiga k = (Knjiga)request.getSession().getAttribute("knjiga");
		List<Integer> invBrojevi = kr.savePrimerci(k, brPrimeraka);
		m.addAttribute("inventarniBrojevi", invBrojevi);
		return "unos/UnosPrimeraka";
	}
	
	
	 @RequestMapping(value = "/getPrimerciZaNaslovIGodinu", method = RequestMethod.GET)
	    public String prikazPrimeraka(String knjiga, String godinaIzdanja, Model m) {
	    	List<Primerak> primerci = pr.getPrimerciZaNaslovIGodinuIzdanja(knjiga, godinaIzdanja);
	    	m.addAttribute("primerci", primerci);
	    	m.addAttribute("naslov", knjiga);
	    	m.addAttribute("godinaIzdanja", godinaIzdanja);
	    	return "unos/PrikazPrimeraka";
	    }
	 
	

}