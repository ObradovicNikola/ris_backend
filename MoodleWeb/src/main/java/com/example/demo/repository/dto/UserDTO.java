package com.example.demo.repository.dto;

public class UserDTO {
	private int idUser;

	private String ime;

	private String prezime;

	private String email;

	private String role;

	public UserDTO() {
		super();
	}

	public UserDTO(int idUser, String ime, String prezime, String email, String role) {
		super();
		this.idUser = idUser;
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.role = role;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
