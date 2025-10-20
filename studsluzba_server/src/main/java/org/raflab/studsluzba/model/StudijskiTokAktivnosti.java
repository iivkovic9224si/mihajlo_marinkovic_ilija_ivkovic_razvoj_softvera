package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class StudijskiTokAktivnosti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipStudijskeAktivnosti tip;
    private Integer godina;
    private LocalDate datum;
    private String napomena;

    @ManyToMany
    private List<Predmet> predmet;

    @ManyToOne
    private StudentIndeks studentIndeks;
}
