package response;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class StudentPodaciResponse {
    private long id;
    @NotNull
    private String ime;	  // not null
    @NotNull
    private String prezime;  // not null
    @NotNull
    private String srednjeIme;   // not null
    private String jmbg;
    @NotNull
    private LocalDate datumRodjenja;  // not null
    private String mestoRodjenja;
    @NotNull
    private String mestoPrebivalista;  // not null
    private String drzavaRodjenja;
    @NotNull
    private String drzavljanstvo;   // not null
    private String nacionalnost;   // samoizjasnjavanje, moze bilo sta
    @NotNull
    private Character pol;    // not null
    @NotNull
    private String adresa;  // not null
    private String brojTelefonaMobilni;
    private String brojTelefonaFiksni;
    @NotNull
    private String email;  // not null
    private String brojLicneKarte;
    private String licnuKartuIzdao;
    private String mestoStanovanja;
    private String adresaStanovanja;   // u toku studija

    public StudentPodaciResponse(Long id, String ime, String prezime, String srednjeIme, String jmbg,
                                LocalDate datumRodjenja, String mestoRodjenja, String mestoPrebivalista,
                                String drzavaRodjenja, String drzavljanstvo, String nacionalnost, Character pol,
                                String adresa, String brojTelefonaMobilni, String brojTelefonaFiksni,
                                String email, String brojLicneKarte, String licnuKartuIzdao,
                                String mestoStanovanja, String adresaStanovanja) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.srednjeIme = srednjeIme;
        this.jmbg = jmbg;
        this.datumRodjenja = datumRodjenja;
        this.mestoRodjenja = mestoRodjenja;
        this.mestoPrebivalista = mestoPrebivalista;
        this.drzavaRodjenja = drzavaRodjenja;
        this.drzavljanstvo = drzavljanstvo;
        this.nacionalnost = nacionalnost;
        this.pol = pol;
        this.adresa = adresa;
        this.brojTelefonaMobilni = brojTelefonaMobilni;
        this.brojTelefonaFiksni = brojTelefonaFiksni;
        this.email = email;
        this.brojLicneKarte = brojLicneKarte;
        this.licnuKartuIzdao = licnuKartuIzdao;
        this.mestoStanovanja = mestoStanovanja;
        this.adresaStanovanja = adresaStanovanja;
    }
}
