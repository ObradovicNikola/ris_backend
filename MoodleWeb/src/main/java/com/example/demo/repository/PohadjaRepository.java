package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import model.Pohadja;

public interface PohadjaRepository extends JpaRepository<Pohadja, Integer> {
	@Transactional
	@Modifying
	@Query("delete from Pohadja p where p.course.idCourse=:idCourse and p.student.idUser=:idStudent")
	public void deleteByCourseAndStudent(@Param("idCourse") Integer idCourse, @Param("idStudent") Integer idStudent);

}
