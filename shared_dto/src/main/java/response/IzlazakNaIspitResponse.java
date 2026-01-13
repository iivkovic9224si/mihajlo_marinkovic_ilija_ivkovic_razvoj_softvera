package response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IzlazakNaIspitResponse {
    private Long id;
    private double brojPoenaSaIspita;
    private double brojPredispitnihPoena;
    private String napomena;
    private boolean ponistava;
    private Long prijavaIspitaID;

    private PrijavaIspitaResponse prijavaIspita;
}
