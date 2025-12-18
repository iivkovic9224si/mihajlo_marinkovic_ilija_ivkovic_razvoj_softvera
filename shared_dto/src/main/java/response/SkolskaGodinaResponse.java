package response;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SkolskaGodinaResponse {
    @NotNull
    private boolean aktivna;
    @NotNull
    private String godina;
}
