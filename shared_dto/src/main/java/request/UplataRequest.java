package request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UplataRequest {
    @NotNull
    private LocalDate datumUplate;
    @NotNull
    private Double iznosUDinarima;
    @NotNull
    private String studentJmbg;
    @NotNull
    private String skolskaGodina; //2025/2026
}
