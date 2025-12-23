package response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RezultatiIspitaResponse {
    private String imeStudenta;
    private String prezimeStudenta;
    private String studProgram;
    private int godinaUpisa;
    private int brojIndeksa;
    private double ukupnoPoena;

    public RezultatiIspitaResponse(String imeStudenta, String prezimeStudenta, String studProgram, int godinaUpisa, int brojIndeksa, double ukupnoPoena) {
        this.imeStudenta = imeStudenta;
        this.prezimeStudenta = prezimeStudenta;
        this.studProgram = studProgram;
        this.godinaUpisa = godinaUpisa;
        this.brojIndeksa = brojIndeksa;
        this.ukupnoPoena = ukupnoPoena;
    }
}
