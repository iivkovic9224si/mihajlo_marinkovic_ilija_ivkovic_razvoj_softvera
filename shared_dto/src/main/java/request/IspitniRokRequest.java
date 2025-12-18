package request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class IspitniRokRequest {

    private Long id;

    @NotNull
    private LocalDate pocetak;

    @NotNull
    private LocalDate kraj; // sada obavezno

    private String godina; // ako je null → koristi se aktivna
}
