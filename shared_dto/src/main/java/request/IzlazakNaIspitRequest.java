package request;

import lombok.Data;


import javax.validation.constraints.NotNull;

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
