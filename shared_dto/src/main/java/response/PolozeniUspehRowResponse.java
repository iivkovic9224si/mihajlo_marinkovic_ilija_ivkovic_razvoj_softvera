package response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolozeniUspehRowResponse {
    private String sifra;
    private String naziv;
    private int espb;
    private double poeni;
    private Integer ocena;
    private LocalDate datum;
    private Integer godinaStudija;
}
