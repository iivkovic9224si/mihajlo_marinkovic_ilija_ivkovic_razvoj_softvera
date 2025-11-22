package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.raflab.studsluzba.model.entity.PredispitneObaveze;
import org.raflab.studsluzba.model.entity.SlusaPredmet;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class OstvareniPredispitniRequest {

    private long id;
    @NotNull
    private double ostvareniPoeni;
    @NotNull
    private PredispitneObavezeRequest predispitneObavezeRequest;
    @NotNull
    private SlusaPredmetRequest slusaPredmetRequest;
}
