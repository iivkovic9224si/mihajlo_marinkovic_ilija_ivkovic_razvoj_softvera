package response;

import enums.VrstaVisokoSkolskihUstanova;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class VisokoSkolskaUstanovaResponse {
    @NotNull
    private String sifra;
    private String naziv;
    private VrstaVisokoSkolskihUstanova vrsta;
}
