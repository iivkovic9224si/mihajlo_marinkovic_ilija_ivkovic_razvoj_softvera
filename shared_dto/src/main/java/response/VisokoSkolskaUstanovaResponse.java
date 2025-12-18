package response;

import enums.VrstaVisokoSkolskihUstanova;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class VisokoSkolskaUstanovaResponse {
    @NotNull
    private String sifra;
    private String naziv;
    private VrstaVisokoSkolskihUstanova vrsta;
}
