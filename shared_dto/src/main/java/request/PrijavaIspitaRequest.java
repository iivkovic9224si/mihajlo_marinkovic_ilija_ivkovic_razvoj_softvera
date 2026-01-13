package request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
