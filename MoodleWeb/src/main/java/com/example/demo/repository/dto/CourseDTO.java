package com.example.demo.repository.dto;

import java.util.ArrayList;
import java.util.List;

public class CourseDTO {
	private int idCourse;
	private String naziv;
	private String opis;
	private String sadrzaj;
	private UserDTO profesor;
	private List<MaterialDTO> materials;
	private List<ObavestenjeDTO> notifications;
	private List<AktivnostDTO> activities;
	private boolean upisan = false;

	public CourseDTO() {
		super();
		this.materials = new ArrayList<>();
	}

	public CourseDTO(int idCourse, String naziv, String opis, String sadrzaj, UserDTO profesor) {
		super();
		this.materials = new ArrayList<>();
		this.idCourse = idCourse;
		this.naziv = naziv;
		this.opis = opis;
		this.sadrzaj = sadrzaj;
		this.profesor = profesor;
	}

	public CourseDTO(int idCourse, String naziv, String opis, String sadrzaj, UserDTO profesor,
			List<MaterialDTO> materials) {
		super();
		this.idCourse = idCourse;
		this.naziv = naziv;
		this.opis = opis;
		this.sadrzaj = sadrzaj;
		this.profesor = profesor;
		this.materials = materials;
	}

	public CourseDTO(int idCourse, String naziv, String opis, String sadrzaj, UserDTO profesor,
			List<MaterialDTO> materials, boolean upisan) {
		super();
		this.idCourse = idCourse;
		this.naziv = naziv;
		this.opis = opis;
		this.sadrzaj = sadrzaj;
		this.profesor = profesor;
		this.materials = materials;
		this.upisan = upisan;
	}

	public CourseDTO(int idCourse, String naziv, String opis, String sadrzaj, UserDTO profesor,
			List<MaterialDTO> materials, List<ObavestenjeDTO> notifications, boolean upisan) {
		super();
		this.idCourse = idCourse;
		this.naziv = naziv;
		this.opis = opis;
		this.sadrzaj = sadrzaj;
		this.profesor = profesor;
		this.materials = materials;
		this.notifications = notifications;
		this.upisan = upisan;
	}

	public CourseDTO(int idCourse, String naziv, String opis, String sadrzaj, UserDTO profesor,
			List<MaterialDTO> materials, List<ObavestenjeDTO> notifications, List<AktivnostDTO> activities,
			boolean upisan) {
		super();
		this.idCourse = idCourse;
		this.naziv = naziv;
		this.opis = opis;
		this.sadrzaj = sadrzaj;
		this.profesor = profesor;
		this.materials = materials;
		this.notifications = notifications;
		this.activities = activities;
		this.upisan = upisan;
	}

	public int getIdCourse() {
		return idCourse;
	}

	public void setIdCourse(int idCourse) {
		this.idCourse = idCourse;
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

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public UserDTO getProfesor() {
		return profesor;
	}

	public void setProfesor(UserDTO profesor) {
		this.profesor = profesor;
	}

	public List<MaterialDTO> getMaterials() {
		return materials;
	}

	public void setMaterials(List<MaterialDTO> materials) {
		this.materials = materials;
	}

	public boolean isUpisan() {
		return upisan;
	}

	public void setUpisan(boolean upisan) {
		this.upisan = upisan;
	}

	public List<ObavestenjeDTO> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<ObavestenjeDTO> notifications) {
		this.notifications = notifications;
	}

	public List<AktivnostDTO> getActivities() {
		return activities;
	}

	public void setActivities(List<AktivnostDTO> activities) {
		this.activities = activities;
	}

}
