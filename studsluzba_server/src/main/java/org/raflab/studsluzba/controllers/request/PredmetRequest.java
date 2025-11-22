package org.raflab.studsluzba.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PredmetRequest {
    @NotNull
    private String sifra;
    @NotNull
    private String naziv;
    private String opis;
    @NotNull
    private Integer espb;
    @NotNull
    private String oznakaStudProgram;
    @NotNull
    private boolean obavezan;
}
