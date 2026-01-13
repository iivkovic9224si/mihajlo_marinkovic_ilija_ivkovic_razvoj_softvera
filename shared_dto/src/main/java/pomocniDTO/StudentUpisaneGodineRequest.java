package pomocniDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpisaneGodineRequest {
    private int godina;
    private int broj;
    private String studProgramOznaka;
}
