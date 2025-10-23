package org.raflab.studsluzba.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class PolozenPredmet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private StudentIndeks studentIndeks;

    @ManyToOne
    private Predmet predmet;

    @OneToOne
    private IzlazakNaIspit izlazakIspita;

    private boolean priznato;

    @ManyToOne
    private VisokoSkolskaUstanova priznatoOdInstitucije;
    private LocalDate datum;
    private double brojPoena;
    private Integer ocena;

    private String napomena;
}
