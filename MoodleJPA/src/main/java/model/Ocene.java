package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Ocene database table.
 * 
 */
@Entity
@NamedQuery(name="Ocene.findAll", query="SELECT o FROM Ocene o")
public class Ocene implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idOcene;

	private int ocena;

	//bi-directional many-to-one association to Aktivnost
	@ManyToOne
	@JoinColumn(name="Aktivnost_idAktivnost")
	private Aktivnost aktivnost;

	//bi-directional many-to-one association to Student
	@ManyToOne
	@JoinColumn(name="idStudent")
	private Student student;

	public Ocene() {
	}

	public int getIdOcene() {
		return this.idOcene;
	}

	public void setIdOcene(int idOcene) {
		this.idOcene = idOcene;
	}

	public int getOcena() {
		return this.ocena;
	}

	public void setOcena(int ocena) {
		this.ocena = ocena;
	}

	public Aktivnost getAktivnost() {
		return this.aktivnost;
	}

	public void setAktivnost(Aktivnost aktivnost) {
		this.aktivnost = aktivnost;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}