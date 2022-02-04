package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Ocene;

public interface OceneRepository extends JpaRepository<Ocene, Integer> {

	@Query("select o from Ocene o where o.student.idUser=:idStudent and o.aktivnost.idAktivnost=:idAktivnost")
	Optional<Ocene> findByStudentAndAktivnost(@Param("idStudent") Integer idStudent,
			@Param("idAktivnost") Integer idAktivnost);

}
