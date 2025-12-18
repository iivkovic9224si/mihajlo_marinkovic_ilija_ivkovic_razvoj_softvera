package request;

import enums.VrstaVisokoSkolskihUstanova;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VisokoSkolskaUstanovaRequest {
    @NotNull
    private String sifra;
    private String naziv;
    private VrstaVisokoSkolskihUstanova vrsta;
}
