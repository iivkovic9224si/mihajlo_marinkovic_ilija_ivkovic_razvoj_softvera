package response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolozenPredmetResponse {
    public StudentIndeksResponse studentIndeksResponse;
    public PredmetResponse predmetResponse;
    public IzlazakNaIspitResponse izlazakNaIspitResponse;

    //< OVA 2 VUKU JEDNO DRUGO AKO JE PRIZNATO MORA IMATI VISOKOSKOLSKU SA KOJE JE PRIZNATO I OBRNUTO
    public VisokoSkolskaUstanovaResponse visokoSkolskaUstanovaResponse;
    public boolean priznato;
    //>
    //Ovo je ili dato ili se izvlaci iz izlazaknaIspitResponse kao datum ispita
    public LocalDate datum;
    public String napomena;
    //Ovo je ili uneto ili se izvlaci iz IzlazakNaIspitResponse
    public double poeni;
    //Ovo se racuna po poenima 51-60=6 61-70=7 71-80=8 itd 5= <51
    public Integer ocena;
}
