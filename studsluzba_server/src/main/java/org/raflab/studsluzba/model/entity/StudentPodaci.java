package org.raflab.studsluzba.model.entity;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

@Entity
@Data
public class StudentPodaci {
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String ime;
    private String prezime;
    private String srednjeIme;
    private String jmbg;
    private LocalDate datumRodjenja;
    private String mestoRodjenja;
    private String mestoPrebivalista;
    private String drzavaRodjenja;
    private String drzavljanstvo;
    private String nacionalnost;   // samoizjasnjavanje, moze bilo sta
    private Character pol;
    private String adresa;
    private String brojTelefonaMobilni;
    private String brojTelefonaFiksni;
    private String email;
    private String brojLicneKarte;
    private String licnuKartuIzdao;
    private String mestoStanovanja;
    private String adresaStanovanja; // u toku studija

    private double uspehIzSrednjeSkole;
    private double poeniSaPrijemnog;

    @ManyToOne
    private SrednjaSkola srednjaSkola;
    @ManyToOne
    private VisokoSkolskaUstanova visokoSkolskaUstanova;
    @OneToMany(mappedBy = "studentPodaci")
    private List<Uplata> uplate;

}
