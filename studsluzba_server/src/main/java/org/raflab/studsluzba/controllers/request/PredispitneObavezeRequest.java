package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.VrstaPredispitneObaveze;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Data
public class PredispitneObavezeRequest {
    private Long id;
    @NotNull
    private int maksimalanBrojPoena;
    @NotNull
    @Enumerated(EnumType.STRING)
    private VrstaPredispitneObaveze vrstaPredispitneObaveze;

    @NotNull
    @ManyToOne
    private PredmetRequest predmetRequest;
}
