package request;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.NotNull;

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
