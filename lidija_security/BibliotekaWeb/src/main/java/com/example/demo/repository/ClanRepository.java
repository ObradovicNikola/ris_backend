package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Clan;

public interface ClanRepository extends JpaRepository<Clan, Integer>{
	
	public List<Clan> findAllByOrderByDatumUpisa();
	
	 @Query("SELECT c FROM Clan c WHERE c.datumUpisa BETWEEN :datOd AND :datDo order by c.datumUpisa")
	 public List<Clan> getClanoviUPeriodu(@Param("datOd")Date datumOd, @Param("datDo")Date datumDo);
}
