package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.raflab.studsluzba.model.entity.IzlazakNaIspit;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.model.entity.VisokoSkolskaUstanova;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolozenPredmetAwayRequest {
    @NotNull
    private int indeksGodina;
    @NotNull
    private int indeksBroj;
    @NotNull
    private String indeksStudProgramOznaka;
    @NotNull
    private String predmetSifra;
    @NotNull
    private String visokoSkolskaUstanovaSifra;
    @NotNull
    private LocalDate datum;
    @NotNull
    private double brojPoena;
    private String napomena;
}
