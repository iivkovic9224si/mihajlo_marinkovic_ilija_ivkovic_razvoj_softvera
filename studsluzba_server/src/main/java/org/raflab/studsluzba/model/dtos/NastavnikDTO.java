package org.raflab.studsluzba.model.dtos;

//Bezbednost (Security / Encapsulation) - password polje
//Ograničavanje JSON problema Lazy loading i Ciklične reference (A → B → A)
//Kontrola nad API verzijama
//Separacija slojeva (Layered Architecture) - DTO služi kao mediator između slojeva.
//DTO može sadržati kombinovane podatke iz više entiteta, izračunate vrednosti, ili filtrirane podatke.

//Entities = predstavljaju bazu, koriste se u servisu i repository sloju.
//DTOs = predstavljaju “ugovor” između backend-a i frontend-a, bezbedni i stabilni.

import lombok.Data;

@Data
public class NastavnikDTO {

	private Long id;
	private String ime;	  // not null
	private String prezime;  // not null
	private String srednjeIme;   // not null 
	private String email;   // not null
	private String brojTelefona;
	private String adresa;
}
