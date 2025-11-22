package org.raflab.studsluzba.controllers.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.raflab.studsluzba.model.entity.Ispit;
import org.raflab.studsluzba.model.entity.Nastavnik;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.SkolskaGodina;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class DrziPredmetRequest {
    private String skolskaGodinaNaziv;
    @NotNull
    private String nastavnikEmail;
    @NotNull
    private String predmetSifra;

}
