package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


/**
 * The persistent class for the KNJIGA database table.
 * 
 */
@Entity
@Table(name="KNJIGA")
@NamedQuery(name="Knjiga.findAll", query="SELECT k FROM Knjiga k")
public class Knjiga implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_KNJIGE")
	private int idKnjige;

	@Column(name="AUTOR")
	private String autor;

	@Column(name="GODINA_IZDANJA")
	private String godinaIzdanja;

	@Column(name="IZDAVAC")
	private String izdavac;

	@Column(name="NASLOV")
	private String naslov;

	//bi-directional many-to-one association to Primerak
	@OneToMany(mappedBy="knjiga")
	private List<Primerak> primeraks;

	public Knjiga() {
	}

	public int getIdKnjige() {
		return this.idKnjige;
	}

	public void setIdKnjige(int idKnjige) {
		this.idKnjige = idKnjige;
	}

	public String getAutor() {
		return this.autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getGodinaIzdanja() {
		return this.godinaIzdanja;
	}

	public void setGodinaIzdanja(String godinaIzdanja) {
		this.godinaIzdanja = godinaIzdanja;
	}

	public String getIzdavac() {
		return this.izdavac;
	}

	public void setIzdavac(String izdavac) {
		this.izdavac = izdavac;
	}

	public String getNaslov() {
		return this.naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	public List<Primerak> getPrimeraks() {
		return this.primeraks;
	}

	public void setPrimeraks(List<Primerak> primeraks) {
		this.primeraks = primeraks;
	}

	public Primerak addPrimerak(Primerak primerak) {
		getPrimeraks().add(primerak);
		primerak.setKnjiga(this);

		return primerak;
	}

	public Primerak removePrimerak(Primerak primerak) {
		getPrimeraks().remove(primerak);
		primerak.setKnjiga(null);

		return primerak;
	}

}