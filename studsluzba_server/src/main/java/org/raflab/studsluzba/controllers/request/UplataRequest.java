package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.model.entity.StudentPodaci;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UplataRequest {
    @NotNull
    private LocalDate datumUplate;
    @NotNull
    private Double iznosUDinarima;
    @NotNull
    private String studentJmbg;
    @NotNull
    private String skolskaGodina; //2025/2026
}
