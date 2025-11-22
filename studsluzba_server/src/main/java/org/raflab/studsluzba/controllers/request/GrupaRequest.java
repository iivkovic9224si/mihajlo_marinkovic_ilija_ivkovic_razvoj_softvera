package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.raflab.studsluzba.controllers.response.PredmetResponse;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupaRequest {
    private long id;
    @NotNull
    private String studijskiProgramOznaka;
    @NotNull
    private List<String> sifrePredmeta;
}
