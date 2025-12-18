package response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OstvareniPredispitniResponse {
    private long id;
    @NotNull
    private double ostvareniPoeni;
    @NotNull
    private PredispitneObavezeResponse predispitneObavezeResponse;
    @NotNull
    private SlusaPredmetResponse slusaPredmetResponse;

    public OstvareniPredispitniResponse(double ostvareniPoeni, PredispitneObavezeResponse predispitneObavezeResponse, SlusaPredmetResponse slusaPredmetResponse) {
        this.ostvareniPoeni = ostvareniPoeni;
        this.predispitneObavezeResponse = predispitneObavezeResponse;
        this.slusaPredmetResponse = slusaPredmetResponse;
    }
}
