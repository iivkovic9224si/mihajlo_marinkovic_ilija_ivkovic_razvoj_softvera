package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import org.raflab.studsluzba.model.entity.PolozenPredmet;
import org.raflab.studsluzba.model.entity.PrijavaIspita;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
