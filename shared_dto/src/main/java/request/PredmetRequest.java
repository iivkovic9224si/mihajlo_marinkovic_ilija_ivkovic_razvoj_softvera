package request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PredmetRequest {
    @NotNull
    private String sifra;
    @NotNull
    private String naziv;
    private String opis;
    @NotNull
    private Integer espb;
    @NotNull
    private String oznakaStudProgram;
    @NotNull
    private boolean obavezan;
}
