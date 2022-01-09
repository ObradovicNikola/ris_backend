package com.example.demo.repository.dto;

public class MaterialDTO {
	private Integer id;
	private String naslov;
	private String putanja;
	private String filetype;

	public MaterialDTO() {
		super();
	}

	public MaterialDTO(Integer id, String naslov, String putanja, String filetype) {
		super();
		this.id = id;
		this.naslov = naslov;
		this.putanja = putanja;
		this.filetype = filetype;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNaslov() {
		return naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	public String getPutanja() {
		return putanja;
	}

	public void setPutanja(String putanja) {
		this.putanja = putanja;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

}
