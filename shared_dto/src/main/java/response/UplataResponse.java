package response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UplataResponse {
    String student_jmbg;
    @NotNull
    private Double iznosUEurima;
    @NotNull
    private Double iznosUDinarima;
}
