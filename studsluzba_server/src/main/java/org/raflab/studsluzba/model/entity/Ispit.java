package org.raflab.studsluzba.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class Ispit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate datumIspita;
    private LocalTime vremePocetkaIspita;
    private boolean zakljucen;

    @ManyToOne
    private DrziPredmet drziPredmet;

    @ManyToOne
    private IspitniRok ispitniRok;

    @OneToMany(mappedBy = "ispit")
    private List<PrijavaIspita> prijavaIspita;
}
