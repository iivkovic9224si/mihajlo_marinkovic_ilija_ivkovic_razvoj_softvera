package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.raflab.studsluzba.controllers.request.NastavnikZvanjeRequest;
import org.raflab.studsluzba.controllers.request.VisokoSkolskaUstanovaRequest;
import org.raflab.studsluzba.model.entity.NastavnikZvanje;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class NastavnikResponse {

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

    private List<NastavnikZvanjeResponse> nastavnikZvanjeResponse;

    private List<DrziPredmetResponse> drziPredmetResponse;

    private LocalDate datumRodjenja;
    private Character pol;
    @NotNull
    private String jmbg;

    private List<VisokoSkolskaUstanovaResponse> visokoSkolskaUstanovaResponses;
}
