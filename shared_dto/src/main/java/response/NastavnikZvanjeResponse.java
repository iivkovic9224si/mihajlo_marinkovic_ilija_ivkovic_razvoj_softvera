package response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class NastavnikZvanjeResponse {
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
    private NastavnikResponse nastavnikResponse;

    public NastavnikZvanjeResponse(LocalDate datumIzbora, String naucnaOblast, String uzaNaucnaOblast, String zvanje, boolean aktivno, NastavnikResponse nastavnikResponse) {
        this.datumIzbora = datumIzbora;
        this.naucnaOblast = naucnaOblast;
        this.uzaNaucnaOblast = uzaNaucnaOblast;
        this.zvanje = zvanje;
        this.aktivno = aktivno;
        this.nastavnikResponse = nastavnikResponse;
    }
}
