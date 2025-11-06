package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.raflab.studsluzba.controllers.request.PredispitneObavezeRequest;
import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class OstvareniPredispitniResponse {
    private long id;
    @NotNull
    private double ostvareniPoeni;
    @NotNull
    private PredispitneObavezeResponse predispitneObavezeResponse;
    @NotNull
    private SlusaPredmetResponse slusaPredmetResponse;

    public OstvareniPredispitniResponse(double ostvareniPoeni, PredispitneObavezeResponse predispitneObavezeResponse, SlusaPredmetResponse slusaPredmetResponse) {
        this.ostvareniPoeni = ostvareniPoeni;
        this.predispitneObavezeResponse = predispitneObavezeResponse;
        this.slusaPredmetResponse = slusaPredmetResponse;
    }
}
