package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudijskiTokAktivnostiRequest {
    private Long id;
    private String tip;
    private Integer godina;
    private LocalDate datum;
    private String napomena;
    private boolean indeksAktivan;

    private int indeksGodina;
    private int indeksBroj;
    private String indeksStudProgramOznaka;
}

