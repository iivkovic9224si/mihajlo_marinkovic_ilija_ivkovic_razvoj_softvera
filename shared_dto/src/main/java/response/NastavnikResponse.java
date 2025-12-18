package response;

import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

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
