package response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class SkolskaGodinaResponse {
    @NotNull
    private boolean aktivna;
    @NotNull
    private String godina;
}
