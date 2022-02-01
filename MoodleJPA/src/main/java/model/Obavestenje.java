package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Obavestenje database table.
 * 
 */
@Entity
@NamedQuery(name="Obavestenje.findAll", query="SELECT o FROM Obavestenje o")
public class Obavestenje implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idObavestenje;

	private int datum;

	private String sadrzaj;

	//bi-directional many-to-one association to Course
	@ManyToOne
	@JoinColumn(name="idCourse")
	private Course course;

	//bi-directional many-to-one association to Procitao
	@OneToMany(mappedBy="obavestenje")
	private List<Procitao> procitaos;

	public Obavestenje() {
	}

	public int getIdObavestenje() {
		return this.idObavestenje;
	}

	public void setIdObavestenje(int idObavestenje) {
		this.idObavestenje = idObavestenje;
	}

	public int getDatum() {
		return this.datum;
	}

	public void setDatum(int datum) {
		this.datum = datum;
	}

	public String getSadrzaj() {
		return this.sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Procitao> getProcitaos() {
		return this.procitaos;
	}

	public void setProcitaos(List<Procitao> procitaos) {
		this.procitaos = procitaos;
	}

	public Procitao addProcitao(Procitao procitao) {
		getProcitaos().add(procitao);
		procitao.setObavestenje(this);

		return procitao;
	}

	public Procitao removeProcitao(Procitao procitao) {
		getProcitaos().remove(procitao);
		procitao.setObavestenje(null);

		return procitao;
	}

}