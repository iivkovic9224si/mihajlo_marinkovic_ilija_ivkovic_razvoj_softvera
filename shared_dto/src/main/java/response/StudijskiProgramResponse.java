package response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudijskiProgramResponse {
    @NotNull
    private String oznaka;// RN, RM
    @NotNull
    private String naziv;
    @NotNull
    private Integer godinaAkreditacije;
    private String zvanje;
    @NotNull
    private Integer trajanjeGodina;
    private Integer trajanjeSemestara;
    @NotNull
    private String vrstaStudija; // OAS - osnovne akademske studje, OSS - osnovne strukovne, 	MAS - master akademske studije
    @NotNull
    private Integer ukupnoEspb;
}
