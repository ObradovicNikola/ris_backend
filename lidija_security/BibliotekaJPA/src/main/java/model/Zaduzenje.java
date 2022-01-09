package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ZADUZENJE database table.
 * 
 */
@Entity
@Table(name="ZADUZENJE")
@NamedQuery(name="Zaduzenje.findAll", query="SELECT z FROM Zaduzenje z")
public class Zaduzenje implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private int id;

	@Temporal(TemporalType.DATE)
	@Column(name="DATUM_VRACANJA")
	private Date datumVracanja;

	@Temporal(TemporalType.DATE)
	@Column(name="DATUM_ZADUZENJA")
	private Date datumZaduzenja;

	//bi-directional many-to-one association to Clan
	@ManyToOne
	@JoinColumn(name="CLANSKIBROJ")
	private Clan clan;

	//bi-directional many-to-one association to Primerak
	@ManyToOne
	@JoinColumn(name="INV_BROJ")
	private Primerak primerak;

	public Zaduzenje() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDatumVracanja() {
		return this.datumVracanja;
	}

	public void setDatumVracanja(Date datumVracanja) {
		this.datumVracanja = datumVracanja;
	}

	public Date getDatumZaduzenja() {
		return this.datumZaduzenja;
	}

	public void setDatumZaduzenja(Date datumZaduzenja) {
		this.datumZaduzenja = datumZaduzenja;
	}

	public Clan getClan() {
		return this.clan;
	}

	public void setClan(Clan clan) {
		this.clan = clan;
	}

	public Primerak getPrimerak() {
		return this.primerak;
	}

	public void setPrimerak(Primerak primerak) {
		this.primerak = primerak;
	}

}