package response;

import enums.VrstaPredispitneObaveze;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;


import javax.validation.constraints.NotNull;

@Data
public class PredispitneObavezeResponse {

    private Long id;
    @NotNull
    private int maksimalanBrojPoena;

    @Enumerated(EnumType.STRING)
    private VrstaPredispitneObaveze vrstaPredispitneObaveze;

    @NotNull
    private PredmetResponse predmetResponse;
}
