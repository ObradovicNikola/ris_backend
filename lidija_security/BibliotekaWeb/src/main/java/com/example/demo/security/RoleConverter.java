package com.example.demo.security;


import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

import com.example.demo.repository.BibliotekaUlogaRepository;

import model.BibliotekaUloga;


public class RoleConverter  implements Converter<String, BibliotekaUloga> {
	
	BibliotekaUlogaRepository r;
	
	public RoleConverter(BibliotekaUlogaRepository r){
		this.r=r;
	}
	
	public BibliotekaUloga convert(String source) {
			  int idRole=-1;
		       try{
		    	   idRole=Integer.parseInt(source);
		       }
		       catch(NumberFormatException e){
		    	   throw new ConversionFailedException(TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(BibliotekaUloga.class),idRole, null);
		       }
		       BibliotekaUloga role=r.getById(idRole);
		      return role;
		  }
}

