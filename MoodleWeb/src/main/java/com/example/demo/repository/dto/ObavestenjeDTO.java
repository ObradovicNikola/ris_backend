package com.example.demo.repository.dto;

public class ObavestenjeDTO {
	private int idObavestenje;
	private int datum;
	private String sadrzaj;
	private int idCourse;

	public ObavestenjeDTO() {
		// TODO Auto-generated constructor stub
	}

	public ObavestenjeDTO(int idObavestenje, int datum, String sadrzaj, int idCourse) {
		super();
		this.idObavestenje = idObavestenje;
		this.datum = datum;
		this.sadrzaj = sadrzaj;
		this.idCourse = idCourse;
	}

	public int getIdObavestenje() {
		return idObavestenje;
	}

	public void setIdObavestenje(int idObavestenje) {
		this.idObavestenje = idObavestenje;
	}

	public int getDatum() {
		return datum;
	}

	public void setDatum(int datum) {
		this.datum = datum;
	}

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public int getIdCourse() {
		return idCourse;
	}

	public void setIdCourse(int idCourse) {
		this.idCourse = idCourse;
	}

}
