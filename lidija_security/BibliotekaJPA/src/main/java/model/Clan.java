package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CLAN database table.
 * 
 */
@Entity
@Table(name="CLAN")
@NamedQuery(name="Clan.findAll", query="SELECT c FROM Clan c")
public class Clan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CLANSKIBROJ")
	private int clanskibroj;

	@Column(name="ADRESA")
	private String adresa;

	@Temporal(TemporalType.DATE)
	@Column(name="DATUM_RODJENJA")
	private Date datumRodjenja;

	@Temporal(TemporalType.DATE)
	@Column(name="DATUM_UPISA")
	private Date datumUpisa;

	@Column(name="IME")
	private String ime;

	@Column(name="PREZIME")
	private String prezime;

	//bi-directional many-to-one association to Kategorija
	@ManyToOne
	@JoinColumn(name="IDKATEGORIJE")
	private Kategorija kategorija;

	//bi-directional many-to-one association to Zaduzenje
	@OneToMany(mappedBy="clan")
	private List<Zaduzenje> zaduzenjes;

	public Clan() {
	}

	public int getClanskibroj() {
		return this.clanskibroj;
	}

	public void setClanskibroj(int clanskibroj) {
		this.clanskibroj = clanskibroj;
	}

	public String getAdresa() {
		return this.adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public Date getDatumRodjenja() {
		return this.datumRodjenja;
	}

	public void setDatumRodjenja(Date datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}

	public Date getDatumUpisa() {
		return this.datumUpisa;
	}

	public void setDatumUpisa(Date datumUpisa) {
		this.datumUpisa = datumUpisa;
	}

	public String getIme() {
		return this.ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return this.prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public Kategorija getKategorija() {
		return this.kategorija;
	}

	public void setKategorija(Kategorija kategorija) {
		this.kategorija = kategorija;
	}

	public List<Zaduzenje> getZaduzenjes() {
		return this.zaduzenjes;
	}

	public void setZaduzenjes(List<Zaduzenje> zaduzenjes) {
		this.zaduzenjes = zaduzenjes;
	}

	public Zaduzenje addZaduzenje(Zaduzenje zaduzenje) {
		getZaduzenjes().add(zaduzenje);
		zaduzenje.setClan(this);

		return zaduzenje;
	}

	public Zaduzenje removeZaduzenje(Zaduzenje zaduzenje) {
		getZaduzenjes().remove(zaduzenje);
		zaduzenje.setClan(null);

		return zaduzenje;
	}

}