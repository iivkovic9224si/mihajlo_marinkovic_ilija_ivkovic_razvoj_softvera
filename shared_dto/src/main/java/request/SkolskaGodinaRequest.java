package request;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SkolskaGodinaRequest {
    @NotNull
    private boolean aktivna;
    @NotNull
    private String godina;
}
