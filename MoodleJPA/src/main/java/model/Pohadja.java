package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Pohadja database table.
 * 
 */
@Entity
@NamedQuery(name="Pohadja.findAll", query="SELECT p FROM Pohadja p")
public class Pohadja implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idPohadja;

	//bi-directional many-to-one association to Course
	@ManyToOne
	@JoinColumn(name="idCourse")
	private Course course;

	//bi-directional many-to-one association to Student
	@ManyToOne
	@JoinColumn(name="idStudent")
	private Student student;

	public Pohadja() {
	}

	public int getIdPohadja() {
		return this.idPohadja;
	}

	public void setIdPohadja(int idPohadja) {
		this.idPohadja = idPohadja;
	}

	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}