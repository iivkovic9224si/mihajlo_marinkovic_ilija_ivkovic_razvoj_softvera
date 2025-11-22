package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import org.raflab.studsluzba.model.entity.VrstaVisokoSkolskihUstanova;

import javax.validation.constraints.NotNull;

@Data
public class VisokoSkolskaUstanovaRequest {
    @NotNull
    private String sifra;
    private String naziv;
    private VrstaVisokoSkolskihUstanova vrsta;
}
