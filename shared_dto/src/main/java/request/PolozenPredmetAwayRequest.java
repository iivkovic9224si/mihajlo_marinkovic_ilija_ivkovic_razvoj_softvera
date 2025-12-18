package request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolozenPredmetAwayRequest {
    @NotNull
    private int indeksGodina;
    @NotNull
    private int indeksBroj;
    @NotNull
    private String indeksStudProgramOznaka;
    @NotNull
    private String predmetSifra;
    @NotNull
    private String visokoSkolskaUstanovaSifra;
    @NotNull
    private LocalDate datum;
    @NotNull
    private double brojPoena;
    private String napomena;
}
