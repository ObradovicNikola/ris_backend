package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Primerak;
import model.Zaduzenje;

public interface ZaduzenjeRepository extends JpaRepository<Zaduzenje, Integer>{
	public List<Zaduzenje> findByPrimerakOrderByDatumZaduzenja(Primerak p);
}
