package request;

import enums.VrstaPredispitneObaveze;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;


import javax.validation.constraints.NotNull;

@Data
public class PredispitneObavezeRequest {
    private Long id;
    @NotNull
    private int maksimalanBrojPoena;
    @NotNull
    @Enumerated(EnumType.STRING)
    private VrstaPredispitneObaveze vrstaPredispitneObaveze;

    @NotNull
    private PredmetRequest predmetRequest;
}
