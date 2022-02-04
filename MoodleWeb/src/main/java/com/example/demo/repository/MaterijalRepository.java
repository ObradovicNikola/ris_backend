package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import model.Materijal;

public interface MaterijalRepository extends JpaRepository<Materijal, Integer> {
	@Transactional
	@Modifying
	@Query("delete from Materijal m where m.naslov=:naslov and m.course.idCourse=:idCourse")
	public void deleteByNaslovAndIdCourse(@Param("naslov") String naslov, @Param("idCourse") Integer idCourse);
}
