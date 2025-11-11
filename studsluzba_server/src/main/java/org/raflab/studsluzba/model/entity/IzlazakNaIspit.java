package org.raflab.studsluzba.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class IzlazakNaIspit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double brojPoenaSaIspita;
    private double brojPredispitnihPoena;
    private String napomena;
    private boolean ponistava;

    @OneToOne
    private PrijavaIspita prijavaIspita;

    @OneToOne
    private PolozenPredmet polozenPredmet;
}
