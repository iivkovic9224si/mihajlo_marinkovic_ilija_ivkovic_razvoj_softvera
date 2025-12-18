package response;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class IspitResponse {
    private Long id;
    @NotNull
    private LocalDate datumIspita;
    @NotNull
    private LocalTime vremePocetkaIspita;
    private boolean zakljucen;

    private DrziPredmetResponse drziPredmetResponse;

    private IspitniRokResponse ispitniRokResponse;
}
