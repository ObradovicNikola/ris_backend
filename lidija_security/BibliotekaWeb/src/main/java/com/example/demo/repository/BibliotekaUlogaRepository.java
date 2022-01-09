package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import model.BibliotekaUloga;

@Repository
@Transactional
public interface BibliotekaUlogaRepository extends JpaRepository<BibliotekaUloga, Integer>{

}
