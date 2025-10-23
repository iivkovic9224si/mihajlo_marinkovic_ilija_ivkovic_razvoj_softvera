package org.raflab.studsluzba.model.dtos;

//Bezbednost (Security / Encapsulation) - password polje
//Ograničavanje JSON problema Lazy loading i Ciklične reference (A → B → A)
//Kontrola nad API verzijama
//Separacija slojeva (Layered Architecture) - DTO služi kao mediator između slojeva.
//DTO može sadržati kombinovane podatke iz više entiteta, izračunate vrednosti, ili filtrirane podatke.

//Entities = predstavljaju bazu, koriste se u servisu i repository sloju.
//DTOs = predstavljaju “ugovor” između backend-a i frontend-a, bezbedni i stabilni.

public class NastavnikDTO {

	private Long id;
	private String ime;	  // not null
	private String prezime;  // not null
	private String srednjeIme;   // not null 
	private String email;   // not null
	private String brojTelefona;
	private String adresa;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getSrednjeIme() {
		return srednjeIme;
	}
	public void setSrednjeIme(String srednjeIme) {
		this.srednjeIme = srednjeIme;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBrojTelefona() {
		return brojTelefona;
	}
	public void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}	
	
	
	
	
}
