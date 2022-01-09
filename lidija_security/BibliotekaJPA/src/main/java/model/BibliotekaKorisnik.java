package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name="BIBLIOTEKA_KORISNIK")
public class BibliotekaKorisnik implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idKorisnik;

	private String korisnickoIme;

	private String sifra;

	private String ime;
	
	private String prezime;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy="korisniks")
	private Set<BibliotekaUloga> ulogas =new HashSet<>();


	public int getIdKorisnik() {
		return idKorisnik;
	}


	public void setIdKorisnik(int idUser) {
		this.idKorisnik = idUser;
	}
	
	
	public String getKorisnickoIme() {
		return korisnickoIme;
	}


	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}


	public String getSifra() {
		return sifra;
	}


	public void setSifra(String sifra) {
		this.sifra = sifra;
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


	public Set<BibliotekaUloga> getUlogas() {
		return ulogas;
	}


	public void setUlogas(Set<BibliotekaUloga> ulogas) {
		this.ulogas = ulogas;
	}


	public void setRoles(Set<BibliotekaUloga> uloge) {
		this.ulogas = uloge;
	}
	
	public void addRole(BibliotekaUloga r) {
		this.ulogas.add(r);
	}
}