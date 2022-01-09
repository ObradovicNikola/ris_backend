package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Course database table.
 * 
 */
@Entity
@NamedQuery(name="Course.findAll", query="SELECT c FROM Course c")
public class Course implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idCourse;

	private String naziv;

	private String opis;

	private String sadrzaj;

	private String sifra;

	//bi-directional many-to-one association to Aktivnost
	@OneToMany(mappedBy="course")
	private List<Aktivnost> aktivnosts;

	//bi-directional many-to-one association to Profesor
	@ManyToOne
	@JoinColumn(name="idProfesor")
	private Profesor profesor;

	//bi-directional many-to-one association to Materijal
	@OneToMany(mappedBy="course")
	private List<Materijal> materijals;

	//bi-directional many-to-one association to Obavestenje
	@OneToMany(mappedBy="course")
	private List<Obavestenje> obavestenjes;

	//bi-directional many-to-one association to Pohadja
	@OneToMany(mappedBy="course")
	private List<Pohadja> pohadjas;

	public Course() {
	}

	public int getIdCourse() {
		return this.idCourse;
	}

	public void setIdCourse(int idCourse) {
		this.idCourse = idCourse;
	}

	public String getNaziv() {
		return this.naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return this.opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getSadrzaj() {
		return this.sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public String getSifra() {
		return this.sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public List<Aktivnost> getAktivnosts() {
		return this.aktivnosts;
	}

	public void setAktivnosts(List<Aktivnost> aktivnosts) {
		this.aktivnosts = aktivnosts;
	}

	public Aktivnost addAktivnost(Aktivnost aktivnost) {
		getAktivnosts().add(aktivnost);
		aktivnost.setCourse(this);

		return aktivnost;
	}

	public Aktivnost removeAktivnost(Aktivnost aktivnost) {
		getAktivnosts().remove(aktivnost);
		aktivnost.setCourse(null);

		return aktivnost;
	}

	public Profesor getProfesor() {
		return this.profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	public List<Materijal> getMaterijals() {
		return this.materijals;
	}

	public void setMaterijals(List<Materijal> materijals) {
		this.materijals = materijals;
	}

	public Materijal addMaterijal(Materijal materijal) {
		getMaterijals().add(materijal);
		materijal.setCourse(this);

		return materijal;
	}

	public Materijal removeMaterijal(Materijal materijal) {
		getMaterijals().remove(materijal);
		materijal.setCourse(null);

		return materijal;
	}

	public List<Obavestenje> getObavestenjes() {
		return this.obavestenjes;
	}

	public void setObavestenjes(List<Obavestenje> obavestenjes) {
		this.obavestenjes = obavestenjes;
	}

	public Obavestenje addObavestenje(Obavestenje obavestenje) {
		getObavestenjes().add(obavestenje);
		obavestenje.setCourse(this);

		return obavestenje;
	}

	public Obavestenje removeObavestenje(Obavestenje obavestenje) {
		getObavestenjes().remove(obavestenje);
		obavestenje.setCourse(null);

		return obavestenje;
	}

	public List<Pohadja> getPohadjas() {
		return this.pohadjas;
	}

	public void setPohadjas(List<Pohadja> pohadjas) {
		this.pohadjas = pohadjas;
	}

	public Pohadja addPohadja(Pohadja pohadja) {
		getPohadjas().add(pohadja);
		pohadja.setCourse(this);

		return pohadja;
	}

	public Pohadja removePohadja(Pohadja pohadja) {
		getPohadjas().remove(pohadja);
		pohadja.setCourse(null);

		return pohadja;
	}

}