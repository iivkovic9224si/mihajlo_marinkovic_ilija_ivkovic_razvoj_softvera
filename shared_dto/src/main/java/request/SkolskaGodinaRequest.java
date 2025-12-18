package request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class SkolskaGodinaRequest {
    @NotNull
    private boolean aktivna;
    @NotNull
    private String godina;
}
