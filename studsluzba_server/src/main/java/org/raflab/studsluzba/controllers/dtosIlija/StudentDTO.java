package org.raflab.studsluzba.controllers.dtosIlija;

public class StudentDTO {
	
	private Long idIndeks;
	private Long idStudentPodaci;
	// dodati jos nesto?
	private String ime;
	private String prezime; 
	// indeks
	private int godinaUpisa;
	private String studProgramOznaka;
	private int broj;
	private boolean aktivanIndeks;
	
	
	
	
	public Long getIdIndeks() {
		return idIndeks;
	}
	public void setIdIndeks(Long idIndeks) {
		this.idIndeks = idIndeks;
	}
	public Long getIdStudentPodaci() {
		return idStudentPodaci;
	}
	public void setIdStudentPodaci(Long idStudentPodaci) {
		this.idStudentPodaci = idStudentPodaci;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public int getGodinaUpisa() {
		return godinaUpisa;
	}
	public void setGodinaUpisa(int godinaUpisa) {
		this.godinaUpisa = godinaUpisa;
	}
	
	
	
	public String getStudProgramOznaka() {
		return studProgramOznaka;
	}
	public void setStudProgramOznaka(String studProgramOznaka) {
		this.studProgramOznaka = studProgramOznaka;
	}
	public int getBroj() {
		return broj;
	}
	public void setBroj(int broj) {
		this.broj = broj;
	}
	public boolean isAktivanIndeks() {
		return aktivanIndeks;
	}
	public void setAktivanIndeks(boolean aktivanIndeks) {
		this.aktivanIndeks = aktivanIndeks;
	}
	
	
	

}
