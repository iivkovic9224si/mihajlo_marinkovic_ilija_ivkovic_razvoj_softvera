package request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolozenPredmetHereRequest {
    @NotNull
    private int indeksGodina;
    @NotNull
    private int indeksBroj;
    @NotNull
    private String indeksStudProgramOznaka;
    @NotNull
    private String predmetSifra;
    @NotNull
    private long izlazakIspitaId;
    private String napomena;
}
