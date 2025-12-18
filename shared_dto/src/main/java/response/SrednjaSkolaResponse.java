package response;

import enums.VrstaSrednjeSkole;
import lombok.AllArgsConstructor;
import lombok.Data;


import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SrednjaSkolaResponse {
    @NotNull
    private String sifra;
    private String naziv;
    private VrstaSrednjeSkole vrstaSrednjeSkole;
}
