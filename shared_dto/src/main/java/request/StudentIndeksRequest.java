package request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentIndeksRequest {

    @NotNull
    private int godina; //ako npr student mastera upisuje neki program, a godina presla u sledecu???
    @NotNull
    private String studProgramOznaka;
    @NotNull
    private int broj;
    private String nacinFinansiranja;
    private boolean aktivan;
    private LocalDate vaziOd;   //TODO - da li da setujm trenutni datum
    private Long studentId;
}