package response;

import enums.VrstaSrednjeSkole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class SrednjaSkolaResponse {
    @NotNull
    private String sifra;
    private String naziv;
    private VrstaSrednjeSkole vrstaSrednjeSkole;
}
