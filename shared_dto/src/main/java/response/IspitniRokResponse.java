package response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class IspitniRokResponse {
    private Long id;
    private LocalDate pocetak;
    private LocalDate kraj;
    private String godina;

    public IspitniRokResponse(LocalDate pocetak, LocalDate kraj, String godina) {
        this.pocetak = pocetak;
        this.kraj = kraj;
        this.godina = godina;
    }

    public IspitniRokResponse(Long id, LocalDate pocetak, LocalDate kraj, String godina) {
        this.id = id;
        this.pocetak = pocetak;
        this.kraj = kraj;
        this.godina = godina;
    }
}
