package com.example.demo.payload;

public class SaveOceneRequest {
	int idStudent;
	int ocena;

	public SaveOceneRequest() {
		super();
	}

	public SaveOceneRequest(int idStudent, int ocena) {
		super();
		this.idStudent = idStudent;
		this.ocena = ocena;
	}

	public int getIdStudent() {
		return idStudent;
	}

	public void setIdStudent(int idStudent) {
		this.idStudent = idStudent;
	}

	public int getOcena() {
		return ocena;
	}

	public void setOcena(int ocena) {
		this.ocena = ocena;
	}

}
