package request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

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
