package request;

import enums.VrstaPredispitneObaveze;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


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
