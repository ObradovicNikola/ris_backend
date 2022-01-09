package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Profesor database table.
 * 
 */
@Entity
@NamedQuery(name="Profesor.findAll", query="SELECT p FROM Profesor p")
public class Profesor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUser;

	//bi-directional many-to-one association to Course
	@OneToMany(mappedBy="profesor")
	private List<Course> courses;

	//bi-directional one-to-one association to User
	@OneToOne
	@JoinColumn(name="idUser")
	private User user;

	public Profesor() {
	}

	public int getIdUser() {
		return this.idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public List<Course> getCourses() {
		return this.courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public Course addCours(Course cours) {
		getCourses().add(cours);
		cours.setProfesor(this);

		return cours;
	}

	public Course removeCours(Course cours) {
		getCourses().remove(cours);
		cours.setProfesor(null);

		return cours;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}