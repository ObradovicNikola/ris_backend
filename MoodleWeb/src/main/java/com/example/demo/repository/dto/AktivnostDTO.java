package com.example.demo.repository.dto;

public class AktivnostDTO {
	private String naziv;
	private String opis;
	private String nazivKursa;
	private int idAktivnost;
	private int maxOcena;
	private int datum; // in unix seconds
	private int ocena; // ako student dobavlja aktivnost, ona ima ocenu

	public AktivnostDTO() {
		super();
	}

	public AktivnostDTO(String naziv, int idAktivnost, int maxOcena, int datum) {
		super();
		this.naziv = naziv;
		this.idAktivnost = idAktivnost;
		this.maxOcena = maxOcena;
		this.datum = datum;
		this.opis = null;
	}

	public AktivnostDTO(String naziv, String opis, int idAktivnost, int maxOcena, int datum) {
		super();
		this.naziv = naziv;
		this.opis = opis;
		this.idAktivnost = idAktivnost;
		this.maxOcena = maxOcena;
		this.datum = datum;
	}

	public AktivnostDTO(String naziv, String opis, String nazivKursa, int idAktivnost, int maxOcena, int datum,
			int ocena) {
		super();
		this.naziv = naziv;
		this.opis = opis;
		this.nazivKursa = nazivKursa;
		this.idAktivnost = idAktivnost;
		this.maxOcena = maxOcena;
		this.datum = datum;
		this.ocena = ocena;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public int getIdAktivnost() {
		return idAktivnost;
	}

	public void setIdAktivnost(int idAktivnost) {
		this.idAktivnost = idAktivnost;
	}

	public int getMaxOcena() {
		return maxOcena;
	}

	public void setMaxOcena(int maxOcena) {
		this.maxOcena = maxOcena;
	}

	public int getDatum() {
		return datum;
	}

	public void setDatum(int datum) {
		this.datum = datum;
	}

	public int getOcena() {
		return ocena;
	}

	public void setOcena(int ocena) {
		this.ocena = ocena;
	}

	public String getNazivKursa() {
		return nazivKursa;
	}

	public void setNazivKursa(String nazivKursa) {
		this.nazivKursa = nazivKursa;
	}

}
