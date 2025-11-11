package org.raflab.studsluzba.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class PredispitneObaveze {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int maksimalanBrojPoena;

    @Enumerated(EnumType.STRING)
    private VrstaPredispitneObaveze vrstaPredispitneObaveze;

    @ManyToOne
    private Predmet predmet;

    @OneToMany(mappedBy = "predispitneObaveze")
    private List<OstvareniPredispitni> ostvareniPredispitni;
}

