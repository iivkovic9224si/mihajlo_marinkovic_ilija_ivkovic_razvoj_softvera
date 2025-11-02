package org.raflab.studsluzba.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Uplata {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private LocalDate datumUplate;
    private Double iznosUDinarima;
    private Double srednjiKurs;

    @ManyToOne
    private StudentPodaci studentPodaci;

    @ManyToOne
    private SkolskaGodina skolskaGodina;
}
