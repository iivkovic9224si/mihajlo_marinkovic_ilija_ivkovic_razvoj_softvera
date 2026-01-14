package pomocniDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UverenjeData {
    private String ime;
    private String prezime;
    private String brojIndeksa;
    private String studijskiProgram;
    private LocalDate datumIzdavanja;
}