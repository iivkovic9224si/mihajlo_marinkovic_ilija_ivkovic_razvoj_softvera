package request;

import enums.VrstaVisokoSkolskihUstanova;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VisokoSkolskaUstanovaRequest {
    @NotNull
    private String sifra;
    private String naziv;
    private VrstaVisokoSkolskihUstanova vrsta;
}
