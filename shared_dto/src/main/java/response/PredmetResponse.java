package response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private Integer godinaStudija;

    public PredmetResponse(String sifra, String naziv, String opis, int espb, boolean obavezan) {
        this.sifra = sifra;
        this.naziv = naziv;
        this.opis = opis;
        this.espb = espb;
        this.obavezan = obavezan;
    }

    public PredmetResponse(Integer espb, Integer godinaStudija, String naziv, boolean obavezan, String opis, String oznakaStudProgram, String sifra) {
        this.espb = espb;
        this.godinaStudija = godinaStudija;
        this.naziv = naziv;
        this.obavezan = obavezan;
        this.opis = opis;
        this.oznakaStudProgram = oznakaStudProgram;
        this.sifra = sifra;
    }
}
