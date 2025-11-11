package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.raflab.studsluzba.model.entity.StudijskiProgram;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupaResponse {
    private long id;
    @NotNull
    private StudijskiProgramResponse studijskiProgram;
    @NotNull
    private List<PredmetResponse> predmetResponses;
}
