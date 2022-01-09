package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the KATEGORIJA database table.
 * 
 */
@Entity
@Table(name="KATEGORIJA")
@NamedQuery(name="Kategorija.findAll", query="SELECT k FROM Kategorija k")
public class Kategorija implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IDKATEGORIJE")
	private int idkategorije;

	@Column(name="NAZIVKATEGORIJE")
	private String nazivkategorije;

	//bi-directional many-to-one association to Clan
	@OneToMany(mappedBy="kategorija")
	private List<Clan> clans;

	public Kategorija() {
	}

	public int getIdkategorije() {
		return this.idkategorije;
	}

	public void setIdkategorije(int idkategorije) {
		this.idkategorije = idkategorije;
	}

	public String getNazivkategorije() {
		return this.nazivkategorije;
	}

	public void setNazivkategorije(String nazivkategorije) {
		this.nazivkategorije = nazivkategorije;
	}

	public List<Clan> getClans() {
		return this.clans;
	}

	public void setClans(List<Clan> clans) {
		this.clans = clans;
	}

	public Clan addClan(Clan clan) {
		getClans().add(clan);
		clan.setKategorija(this);

		return clan;
	}

	public Clan removeClan(Clan clan) {
		getClans().remove(clan);
		clan.setKategorija(null);

		return clan;
	}

}