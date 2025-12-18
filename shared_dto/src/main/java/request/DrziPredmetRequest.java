package request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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
