package org.raflab.studsluzba.controllers.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.raflab.studsluzba.model.entity.Predmet;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class StudijskiProgramRequest {

    @NotNull
    private String oznaka;// RN, RM
    private String naziv;
    private Integer godinaAkreditacije;
    private String zvanje;
    private Integer trajanjeGodina;
    private Integer trajanjeSemestara;
    private String vrstaStudija; // OAS - osnovne akademske studje, OSS - osnovne strukovne, 	MAS - master akademske studije
    private Integer ukupnoEspb;

}
