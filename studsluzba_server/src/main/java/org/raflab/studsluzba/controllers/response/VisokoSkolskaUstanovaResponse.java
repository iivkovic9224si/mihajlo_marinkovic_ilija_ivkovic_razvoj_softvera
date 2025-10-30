package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.raflab.studsluzba.model.entity.Nastavnik;
import org.raflab.studsluzba.model.entity.PolozenPredmet;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.model.entity.VrstaVisokoSkolskihUstanova;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class VisokoSkolskaUstanovaResponse {
    @NotNull
    private String sifra;
    private String naziv;
    private VrstaVisokoSkolskihUstanova vrsta;
}
