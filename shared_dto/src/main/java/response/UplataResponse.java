package response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;


@Data
public class UplataResponse {
    private Long id;
    private String studentJmbg;
    private LocalDate datumUplate;
    private Double iznosUDinarima;
    private Double iznosUEurima;
}
