package response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
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