package com.example.demo.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Course;
import model.User;

public interface CourseRepository extends JpaRepository<Course, Integer> {
	@Query("select c from Course c join c.pohadjas p where p.student.idUser=:idUser")
	public List<Course> findMyCoursesAsAStudent(@Param("idUser") Integer idUser);

	@Query("select c from Course c where c.profesor.idUser=:idUser")
	public List<Course> findMyCoursesAsAProfesor(@Param("idUser") Integer idUser);
}
