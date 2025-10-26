package org.raflab.studsluzba.controllers.response;

import org.raflab.studsluzba.model.entity.StudentIndeks;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class StudijskiTokAktivnostiResponse {
    @NotNull
    private String tip;
    @NotNull
    private Integer godina;
    @NotNull
    private LocalDate datum;
    private String napomena;
    @NotNull
    private StudentIndeksResponse studentIndeksResponse;

    public StudijskiTokAktivnostiResponse(String tip, Integer godina, LocalDate datum, String napomena, StudentIndeksResponse studentIndeksResponse) {
        this.tip = tip;
        this.godina = godina;
        this.datum = datum;
        this.napomena = napomena;
        this.studentIndeksResponse = new StudentIndeksResponse();
    }
}
