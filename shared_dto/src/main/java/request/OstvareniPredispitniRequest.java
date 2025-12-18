package request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class OstvareniPredispitniRequest {

    private long id;
    @NotNull
    private double ostvareniPoeni;
    @NotNull
    private PredispitneObavezeRequest predispitneObavezeRequest;
    @NotNull
    private SlusaPredmetRequest slusaPredmetRequest;
}
