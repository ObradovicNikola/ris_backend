package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name="BIBLIOTEKA_ULOGA")
public class BibliotekaUloga implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idUloga")
	private int idUloga;

	private String naziv;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="BIBLIOTEKA_KORISNIK_ULOGA", joinColumns = @JoinColumn(name = "idUloga",referencedColumnName = "idUloga"),inverseJoinColumns = @JoinColumn(name = "idKorisnik"))
	private Set<BibliotekaKorisnik> korisniks =new HashSet<>();

	public int getIdUloga() {
		return idUloga;
	}

	public void setIdUloga(int idUloga) {
		this.idUloga = idUloga;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Set<BibliotekaKorisnik> getKorisniks() {
		return korisniks;
	}

	public void setKorisniks(Set<BibliotekaKorisnik> korisniks) {
		this.korisniks = korisniks;
	}
	public void addKorisnik(BibliotekaKorisnik k) {
		this.korisniks.add(k);
	}

}