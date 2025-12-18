package request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class UpisStudentaRequest {
    @NotNull
    private StudijskiTokAktivnostiRequest studijskiTokAktivnostiRequest;
    @NotNull
    private List<DrziPredmetRequest> drziPredmetRequests;
}
