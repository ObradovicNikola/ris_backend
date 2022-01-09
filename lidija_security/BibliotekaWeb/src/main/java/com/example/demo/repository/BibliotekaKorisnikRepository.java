package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import model.BibliotekaKorisnik;


@Repository
@Transactional
public interface BibliotekaKorisnikRepository extends JpaRepository<BibliotekaKorisnik, Integer>{
	BibliotekaKorisnik findByKorisnickoIme(String korisnickoIme);
}
