package response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupaResponse {
    private long id;
    @NotNull
    private StudijskiProgramResponse studijskiProgram;
    @NotNull
    private List<PredmetResponse> predmetResponses;
}
