package com.example.demo.security.models;

public class RegistrationRequest {
	private String email;
	private String password;
	private String ime;
	private String prezime;
	private Integer idRole;

	public RegistrationRequest() {
		super();
	}

	public RegistrationRequest(String email, String password, String ime, String prezime, Integer idRole) {
		super();
		this.email = email;
		this.password = password;
		this.ime = ime;
		this.prezime = prezime;
		this.idRole = idRole;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Integer getIdRole() {
		return idRole;
	}

	public void setIdRole(Integer idRole) {
		this.idRole = idRole;
	}

}
