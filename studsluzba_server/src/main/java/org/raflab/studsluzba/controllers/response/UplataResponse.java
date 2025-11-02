package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.model.entity.StudentPodaci;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UplataResponse {
    @NotNull
    private Double iznosUEurima;
    @NotNull
    private Double iznosUDinarima;
}
