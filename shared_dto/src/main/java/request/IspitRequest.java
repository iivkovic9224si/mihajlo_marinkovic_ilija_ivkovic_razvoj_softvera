package request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class IspitRequest {
    private Long id;

    @NotNull
    private LocalDate datumIspita;

    @NotNull
    private LocalTime vremePocetkaIspita;

    @NotNull
    private Integer trajanjeUMinutama; // novo polje

    private boolean zakljucen;

    @NotNull
    private DrziPredmetRequest drziPredmetRequest;

    @NotNull
    private IspitniRokRequest ispitniRokRequest;
}
