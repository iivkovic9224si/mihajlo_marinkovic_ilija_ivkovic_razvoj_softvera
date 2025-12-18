package request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class NastavnikZvanjeRequest {
    private Long id;
    @NotNull
    private LocalDate datumIzbora;
    @NotNull
    private String naucnaOblast;
    @NotNull
    private String uzaNaucnaOblast;
    @NotNull
    private String zvanje;
    @NotNull
    private boolean aktivno;
    @NotNull
    private NastavnikRequest nastavnikRequest;

    // Prazan konstruktor potreban za Jackson
    public NastavnikZvanjeRequest() {}

    public NastavnikZvanjeRequest(LocalDate datumIzbora, String naucnaOblast, String uzaNaucnaOblast, String zvanje, boolean aktivno, NastavnikRequest nastavnikRequest) {
        this.datumIzbora = datumIzbora;
        this.naucnaOblast = naucnaOblast;
        this.uzaNaucnaOblast = uzaNaucnaOblast;
        this.zvanje = zvanje;
        this.aktivno = aktivno;
        this.nastavnikRequest = nastavnikRequest;
    }
}

