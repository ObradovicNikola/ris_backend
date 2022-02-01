package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Aktivnost;

public interface AktivnostRepository extends JpaRepository<Aktivnost, Integer> {

}
