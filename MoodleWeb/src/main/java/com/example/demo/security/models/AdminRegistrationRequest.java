package com.example.demo.security.models;

public class AdminRegistrationRequest {
	private String email;
	private String password;
	private String ime;
	private String prezime;
	private Integer idRole;
	private boolean enabled;

	public AdminRegistrationRequest() {
		super();
	}

	public AdminRegistrationRequest(String email, String password, String ime, String prezime, Integer idRole) {
		super();
		this.email = email;
		this.password = password;
		this.ime = ime;
		this.prezime = prezime;
		this.idRole = idRole;
	}

	public AdminRegistrationRequest(String email, String password, String ime, String prezime, Integer idRole,
			boolean enabled) {
		super();
		this.email = email;
		this.password = password;
		this.ime = ime;
		this.prezime = prezime;
		this.idRole = idRole;
		this.enabled = enabled;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
