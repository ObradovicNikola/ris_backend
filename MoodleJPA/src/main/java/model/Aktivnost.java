package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Aktivnost database table.
 * 
 */
@Entity
@NamedQuery(name="Aktivnost.findAll", query="SELECT a FROM Aktivnost a")
public class Aktivnost implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idAktivnost;

	@Temporal(TemporalType.DATE)
	private Date datum;

	private int maxBrPoena;

	private String naziv;

	private String opis;

	//bi-directional many-to-one association to Course
	@ManyToOne
	@JoinColumn(name="idCourse")
	private Course course;

	//bi-directional many-to-one association to Ocene
	@OneToMany(mappedBy="aktivnost")
	private List<Ocene> ocenes;

	public Aktivnost() {
	}

	public int getIdAktivnost() {
		return this.idAktivnost;
	}

	public void setIdAktivnost(int idAktivnost) {
		this.idAktivnost = idAktivnost;
	}

	public Date getDatum() {
		return this.datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public int getMaxBrPoena() {
		return this.maxBrPoena;
	}

	public void setMaxBrPoena(int maxBrPoena) {
		this.maxBrPoena = maxBrPoena;
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

	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Ocene> getOcenes() {
		return this.ocenes;
	}

	public void setOcenes(List<Ocene> ocenes) {
		this.ocenes = ocenes;
	}

	public Ocene addOcene(Ocene ocene) {
		getOcenes().add(ocene);
		ocene.setAktivnost(this);

		return ocene;
	}

	public Ocene removeOcene(Ocene ocene) {
		getOcenes().remove(ocene);
		ocene.setAktivnost(null);

		return ocene;
	}

}