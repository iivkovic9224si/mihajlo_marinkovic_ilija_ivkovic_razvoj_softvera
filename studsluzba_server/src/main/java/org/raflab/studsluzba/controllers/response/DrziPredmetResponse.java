package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrziPredmetResponse {
    @NotNull
    private String skolskaGodinaNaziv;
    @NotNull
    private String nastavnikEmail;
    @NotNull
    private String predmetSifra;
}
