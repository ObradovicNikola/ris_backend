package com.example.demo.payload;

public class GetOceneResponse {
	int idStudent;
	String ime;
	String prezime;
	int ocena;

	public GetOceneResponse() {
		super();
	}

	public GetOceneResponse(int idStudent, String ime, String prezime, int ocena) {
		super();
		this.idStudent = idStudent;
		this.ime = ime;
		this.prezime = prezime;
		this.ocena = ocena;
	}

	public int getIdStudent() {
		return idStudent;
	}

	public void setIdStudent(int idStudent) {
		this.idStudent = idStudent;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public int getOcena() {
		return ocena;
	}

	public void setOcena(int ocena) {
		this.ocena = ocena;
	}

}
