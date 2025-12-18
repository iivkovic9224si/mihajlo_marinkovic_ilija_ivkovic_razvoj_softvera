package request;

import enums.VrstaSrednjeSkole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SrednjaSkolaRequest {
    @NotNull
    private String sifra;
    private String naziv;
    private VrstaSrednjeSkole vrstaSrednjeSkole;
}
