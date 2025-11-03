package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import lombok.NonNull;
import net.minidev.json.annotate.JsonIgnore;
import org.raflab.studsluzba.controllers.response.NastavnikZvanjeResponse;
import org.raflab.studsluzba.model.entity.NastavnikZvanje;
import org.raflab.studsluzba.model.entity.VisokoSkolskaUstanova;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class NastavnikRequest {

    private Long id;
    @NotNull
    private String ime;
    @NotNull
    private String prezime;
    @NotNull
    private String srednjeIme;
    @NotNull
    private String email;
    private String brojTelefona;
    private String adresa;

    private List<NastavnikZvanjeRequest> nastavnikZvanjeRequests;

    private List<DrziPredmetRequest> drziPredmetRequests;

    private LocalDate datumRodjenja;
    private Character pol;
    @NotNull
    private String jmbg;

    private List<VisokoSkolskaUstanovaRequest> visokoSkolskaUstanovaRequests;
}
