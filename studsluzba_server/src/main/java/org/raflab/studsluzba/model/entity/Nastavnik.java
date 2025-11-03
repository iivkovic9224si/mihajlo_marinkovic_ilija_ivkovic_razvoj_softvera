package org.raflab.studsluzba.model.entity;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@Data
public class Nastavnik {
	 
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String ime;
    private String prezime;
    private String srednjeIme;
    private String email;
    private String brojTelefona;
    private String adresa;

    @OneToMany
    private List<NastavnikZvanje> zvanja;

    @OneToMany
    private List<DrziPredmet> drziPredmet;
	 
    private LocalDate datumRodjenja;
    private Character pol;
    private String jmbg;

    @ManyToMany
    private List<VisokoSkolskaUstanova> visokoSkolskaUstanova;
}
