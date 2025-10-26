package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.raflab.studsluzba.model.entity.StudentIndeks;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class StudijskiTokAktivnostiRequest {
    @NotNull
    private String tip;
    @NotNull
    private Integer godina;
    private LocalDate datum;
    private String napomena;

    @NotNull
    private int indeksGodina;
    @NotNull
    private int indeksBroj;
    @NotNull
    private String indeksStudProgramOznaka;
    @NotNull
    private boolean indeksAktivan;
}
