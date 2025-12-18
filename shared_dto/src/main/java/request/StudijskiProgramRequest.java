package request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class StudijskiProgramRequest {
    @NotNull
    private String oznaka;// RN, RM
    private String naziv;
    private Integer godinaAkreditacije;
    private String zvanje;
    private Integer trajanjeGodina;
    private Integer trajanjeSemestara;
    @NotNull
    private String vrstaStudija; // OAS - osnovne akademske studje, OSS - osnovne strukovne, 	MAS - master akademske studije
    private Integer ukupnoEspb;
}
