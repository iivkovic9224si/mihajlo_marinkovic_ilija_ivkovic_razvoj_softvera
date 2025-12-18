package request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class StudijskiTokAktivnostiRequest {
    @NotNull
    private String tip;
    @NotNull
    private Integer godina;
    @NotNull
    private LocalDate datum;
    private String napomena;
    private boolean indeksAktivan;

    @NotNull
    private int indeksGodina;
    @NotNull
    private int indeksBroj;
    @NotNull
    private String indeksStudProgramOznaka;

}
