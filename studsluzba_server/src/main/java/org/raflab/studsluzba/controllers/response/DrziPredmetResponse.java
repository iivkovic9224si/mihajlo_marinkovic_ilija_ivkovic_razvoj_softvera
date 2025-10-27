package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrziPredmetResponse {
    private String predmetSifra;
    private String nastavnikEmail;
    private String skolskaGodinaNaziv;
}
