package response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PrijavaIspitaResponse {
    private long id;
    @NotNull
    private LocalDate datumPrijave;
    @NotNull
    private IspitResponse ispitResponse;
    @NotNull
    private SlusaPredmetResponse slusaPredmetResponse;
}