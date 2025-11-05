package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class IspitniRokRequest {

    private Long id;

    @NotNull
    private LocalDate pocetak;

    @NotNull
    private LocalDate kraj; // sada obavezno

    private String godina; // ako je null → koristi se aktivna
}
