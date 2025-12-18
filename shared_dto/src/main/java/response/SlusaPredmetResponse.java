package response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SlusaPredmetResponse {
    private long id;
    private String skolskaGodina;
    private String studentIndeks;
    private String nastavnik;
    private String predmet;
    public SlusaPredmetResponse(String skolskaGodina, String studentIndeks, String nastavnik, String predmet) {
        this.skolskaGodina = skolskaGodina;
        this.studentIndeks = studentIndeks;
        this.nastavnik = nastavnik;
        this.predmet = predmet;
    }

}
