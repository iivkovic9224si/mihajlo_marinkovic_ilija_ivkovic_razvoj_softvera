package response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PredmetResponse {

    @NotNull
    private String sifra;
    @NotNull
    private String naziv;
    private String opis;
    @NotNull
    private Integer espb;
    @NotNull
    private String oznakaStudProgram;
    @NotNull
    private boolean obavezan;

    public PredmetResponse(String sifra, String naziv, String opis, int espb, boolean obavezan) {
        this.sifra = sifra;
        this.naziv = naziv;
        this.opis = opis;
        this.espb = espb;
        this.obavezan = obavezan;
    }
}
