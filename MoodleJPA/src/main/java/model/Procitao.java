package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Procitao database table.
 * 
 */
@Entity
@NamedQuery(name="Procitao.findAll", query="SELECT p FROM Procitao p")
public class Procitao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idProcitao;

	private boolean procitao;

	//bi-directional many-to-one association to Obavestenje
	@ManyToOne
	@JoinColumn(name="idObavestenje")
	private Obavestenje obavestenje;

	//bi-directional many-to-one association to Student
	@ManyToOne
	@JoinColumn(name="idStudent")
	private Student student;

	public Procitao() {
	}

	public int getIdProcitao() {
		return this.idProcitao;
	}

	public void setIdProcitao(int idProcitao) {
		this.idProcitao = idProcitao;
	}

	public boolean getProcitao() {
		return this.procitao;
	}

	public void setProcitao(boolean procitao) {
		this.procitao = procitao;
	}

	public Obavestenje getObavestenje() {
		return this.obavestenje;
	}

	public void setObavestenje(Obavestenje obavestenje) {
		this.obavestenje = obavestenje;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}