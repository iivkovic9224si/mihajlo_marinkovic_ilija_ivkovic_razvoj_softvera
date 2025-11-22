package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PrijavaIspitaResponse {
    private long id;
    @NotNull
    private LocalDate datumPrijave;
    @NotNull
    private IspitResponse ispitResponse;
    @NotNull
    private SlusaPredmetResponse slusaPredmetResponse;
}