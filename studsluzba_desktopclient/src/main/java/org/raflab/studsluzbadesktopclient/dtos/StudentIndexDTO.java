package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Data
public class StudentIndexDTO {
    private Long id;
    private int broj;
    private int godina;
    private String studProgramOznaka;
    private String nacinFinansiranja;
    private boolean aktivan;
    private LocalDate vaziOd;
    private StudentDTO student;
    private StudProgramDTO studProgramDTO;
    private Integer ostvarenoEspb;
}
