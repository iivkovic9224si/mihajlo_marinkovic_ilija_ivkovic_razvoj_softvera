package request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StudijskiProgramNewRequest {

    @NotNull private String oznaka;
    @NotNull private String naziv;
    @NotNull private Integer godinaAkreditacije;
    private String zvanje;
    @NotNull private Integer trajanjeGodina;
    private Integer trajanjeSemestara;
    @NotNull private String vrstaStudija;
    @NotNull private Integer ukupnoEspb;
}

