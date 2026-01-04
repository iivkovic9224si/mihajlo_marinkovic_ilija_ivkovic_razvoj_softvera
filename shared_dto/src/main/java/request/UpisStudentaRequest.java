package request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpisStudentaRequest {
    @NotNull
    private StudijskiTokAktivnostiRequest studijskiTokAktivnostiRequest;
    @NotNull
    private List<DrziPredmetRequest> drziPredmetRequests;
}
