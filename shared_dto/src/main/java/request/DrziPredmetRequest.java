package request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrziPredmetRequest {
    private String skolskaGodinaNaziv;
    @NotNull
    private String nastavnikEmail;
    @NotNull
    private String predmetSifra;

}
