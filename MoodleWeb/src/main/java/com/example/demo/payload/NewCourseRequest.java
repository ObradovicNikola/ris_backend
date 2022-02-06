package com.example.demo.payload;

public class NewCourseRequest {
	private String naziv;
	private String opis;
	private String sadrzaj;
	private String sifra;
	private Integer idProfesor;

	public NewCourseRequest() {
		super();
	}

	public NewCourseRequest(String naziv, String opis, String sadrzaj, String sifra) {
		super();
		this.naziv = naziv;
		this.opis = opis;
		this.sadrzaj = sadrzaj;
		this.sifra = sifra;
	}

	public NewCourseRequest(String naziv, String opis, String sadrzaj, String sifra, Integer idProfesor) {
		super();
		this.naziv = naziv;
		this.opis = opis;
		this.sadrzaj = sadrzaj;
		this.sifra = sifra;
		this.idProfesor = idProfesor;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public Integer getIdProfesor() {
		return idProfesor;
	}

	public void setIdProfesor(Integer idProfesor) {
		this.idProfesor = idProfesor;
	}

}
