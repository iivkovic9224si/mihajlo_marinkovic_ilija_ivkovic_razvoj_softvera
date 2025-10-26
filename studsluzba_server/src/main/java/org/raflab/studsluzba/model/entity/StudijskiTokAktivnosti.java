package org.raflab.studsluzba.model.entity;

import lombok.AllArgsConstructor;
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
    private VrstaStudijskeAktivnosti tip;
    private Integer godina;
    private LocalDate datum;
    private String napomena;

    @ManyToMany
    private List<Predmet> predmet;

    @ManyToOne
    private StudentIndeks studentIndeks;

    public StudijskiTokAktivnosti() {
    }

    public StudijskiTokAktivnosti(VrstaStudijskeAktivnosti tip, Integer godina, LocalDate datum, String napomena, StudentIndeks studentIndeks) {
        this.tip = tip;
        this.godina = godina;
        this.datum = datum;
        this.napomena = napomena;
        this.studentIndeks = studentIndeks;
    }
}
