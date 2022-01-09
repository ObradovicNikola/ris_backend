package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Materijal database table.
 * 
 */
@Entity
@NamedQuery(name="Materijal.findAll", query="SELECT m FROM Materijal m")
public class Materijal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idMaterijal;

	private String contentType;

	private String naslov;

	private String putanja;

	//bi-directional many-to-one association to Course
	@ManyToOne
	@JoinColumn(name="idCourse")
	private Course course;

	public Materijal() {
	}

	public int getIdMaterijal() {
		return this.idMaterijal;
	}

	public void setIdMaterijal(int idMaterijal) {
		this.idMaterijal = idMaterijal;
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getNaslov() {
		return this.naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	public String getPutanja() {
		return this.putanja;
	}

	public void setPutanja(String putanja) {
		this.putanja = putanja;
	}

	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}