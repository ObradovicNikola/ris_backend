package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Primerak;

public interface PrimerakRepository extends JpaRepository<Primerak, Integer> {
	@Query("select p from Primerak p where p.knjiga.naslov like :naslov"
			+ " and p.knjiga.godinaIzdanja like :godIzd")
	public List<Primerak> getPrimerciZaNaslovIGodinuIzdanja(
				@Param("naslov") String naslov,
				@Param("godIzd") String godinaIzdanja);
}
