package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.raflab.studsluzba.model.entity.PredispitneObaveze;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.raflab.studsluzba.model.entity.StudijskiTokAktivnosti;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PredmetResponse {

    @NotNull
    private String sifra;
    @NotNull
    private String naziv;
    private String opis;
    @NotNull
    private Integer espb;

    //private StudijskiProgramResponse studProgram;

    @NotNull
    private boolean obavezan;

    public PredmetResponse(String sifra, String naziv, String opis, Integer espb, boolean obavezan) {
        this.sifra = sifra;
        this.naziv = naziv;
        this.opis = opis;
        this.espb = espb;
        this.obavezan = obavezan;
    }

}
