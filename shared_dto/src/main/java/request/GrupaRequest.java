package request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupaRequest {
    private long id;
    @NotNull
    private String studijskiProgramOznaka;
    @NotNull
    private List<String> sifrePredmeta;
}
