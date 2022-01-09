package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Student database table.
 * 
 */
@Entity
@NamedQuery(name="Student.findAll", query="SELECT s FROM Student s")
public class Student implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUser;

	//bi-directional many-to-one association to Ocene
	@OneToMany(mappedBy="student")
	private List<Ocene> ocenes;

	//bi-directional many-to-one association to Pohadja
	@OneToMany(mappedBy="student")
	private List<Pohadja> pohadjas;

	//bi-directional many-to-one association to Procitao
	@OneToMany(mappedBy="student")
	private List<Procitao> procitaos;

	//bi-directional one-to-one association to User
	@OneToOne
	@JoinColumn(name="idUser")
	private User user;

	public Student() {
	}

	public int getIdUser() {
		return this.idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public List<Ocene> getOcenes() {
		return this.ocenes;
	}

	public void setOcenes(List<Ocene> ocenes) {
		this.ocenes = ocenes;
	}

	public Ocene addOcene(Ocene ocene) {
		getOcenes().add(ocene);
		ocene.setStudent(this);

		return ocene;
	}

	public Ocene removeOcene(Ocene ocene) {
		getOcenes().remove(ocene);
		ocene.setStudent(null);

		return ocene;
	}

	public List<Pohadja> getPohadjas() {
		return this.pohadjas;
	}

	public void setPohadjas(List<Pohadja> pohadjas) {
		this.pohadjas = pohadjas;
	}

	public Pohadja addPohadja(Pohadja pohadja) {
		getPohadjas().add(pohadja);
		pohadja.setStudent(this);

		return pohadja;
	}

	public Pohadja removePohadja(Pohadja pohadja) {
		getPohadjas().remove(pohadja);
		pohadja.setStudent(null);

		return pohadja;
	}

	public List<Procitao> getProcitaos() {
		return this.procitaos;
	}

	public void setProcitaos(List<Procitao> procitaos) {
		this.procitaos = procitaos;
	}

	public Procitao addProcitao(Procitao procitao) {
		getProcitaos().add(procitao);
		procitao.setStudent(this);

		return procitao;
	}

	public Procitao removeProcitao(Procitao procitao) {
		getProcitaos().remove(procitao);
		procitao.setStudent(null);

		return procitao;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}