package org.raflab.studsluzba.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OstvareniPredispitni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double ostvareniPoeni;

    @ManyToOne
    private PredispitneObaveze predispitneObaveze;

    @ManyToOne
    private SlusaPredmet slusaPredmet;

}
