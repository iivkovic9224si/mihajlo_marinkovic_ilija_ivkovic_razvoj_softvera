package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrziPredmetResponse {
    private String skolskaGodinaNaziv;
    private String nastavnikEmail;
    private String predmetSifra;
}
