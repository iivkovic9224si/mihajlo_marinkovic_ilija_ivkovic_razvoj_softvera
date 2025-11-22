package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SlusaPredmetRequest {

    private long id;
    //Skolska godina
    @NotNull
    private String skolskaGodina;
    //Student Indeks
    @NotNull
    private String studProgramOznaka;
    @NotNull
    private int studentBroj;
    @NotNull
    private int studentGodina;
    //Drzi Predmet
    @NotNull
    private String nastavnikMail;
    @NotNull
    private String sifraPredmeta;

}
