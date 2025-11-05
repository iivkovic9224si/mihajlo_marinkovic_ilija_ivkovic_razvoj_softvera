package org.raflab.studsluzba.controllers.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.raflab.studsluzba.model.entity.OstvareniPredispitni;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.VrstaPredispitneObaveze;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PredispitneObavezeResponse {

    private Long id;
    @NotNull
    private int maksimalanBrojPoena;

    @Enumerated(EnumType.STRING)
    private VrstaPredispitneObaveze vrstaPredispitneObaveze;

    @NotNull
    @ManyToOne
    private PredmetResponse predmetResponse;
}
