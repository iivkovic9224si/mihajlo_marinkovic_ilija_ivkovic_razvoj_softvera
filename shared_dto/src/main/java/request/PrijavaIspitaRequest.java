package request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PrijavaIspitaRequest {
    private long id;
    @NotNull
    private LocalDate datumPrijave;
    @NotNull
    private long ispitID;
    @NotNull
    private int indeksGodina;
    @NotNull
    private int indeksBroj;
    @NotNull
    private String indeksStudProgramOznaka;
}
