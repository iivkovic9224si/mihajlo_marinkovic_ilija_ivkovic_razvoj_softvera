package response;

import lombok.Data;


import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UplataResponse {
    String student_jmbg;
    @NotNull
    private Double iznosUEurima;
    @NotNull
    private Double iznosUDinarima;
}
