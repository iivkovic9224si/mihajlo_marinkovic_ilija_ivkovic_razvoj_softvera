package request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IzlazakNaIspitRequest {
    private Long id;
    @NotNull
    private double brojPoenaSaIspita;
    @NotNull
    private double brojPredispitnihPoena;
    private String napomena;
    private boolean ponistava;
    @NotNull
    private long prijavaIspitaID;
}
