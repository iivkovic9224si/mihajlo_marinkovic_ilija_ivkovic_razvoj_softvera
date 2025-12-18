package request;

import enums.VrstaSrednjeSkole;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class SrednjaSkolaRequest {
    @NotNull
    private String sifra;
    private String naziv;
    private VrstaSrednjeSkole vrstaSrednjeSkole;
}
