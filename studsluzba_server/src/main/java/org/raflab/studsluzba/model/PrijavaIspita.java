package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PrijavaIspita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Ispit ispit;

    @ManyToOne
    private SlusaPredmet slusaPredmet;
}
