package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.controllers.request.IspitniRokRequest;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class IspitResponse {
    private Long id;
    @NotNull
    private LocalDate datumIspita;
    @NotNull
    private LocalTime vremePocetkaIspita;
    private boolean zakljucen;

    private DrziPredmetResponse drziPredmetResponse;

    private IspitniRokResponse ispitniRokResponse;
}
