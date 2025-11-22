package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.GrupaRequest;
import org.raflab.studsluzba.controllers.response.GrupaResponse;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.entity.Grupa;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;

import java.util.ArrayList;
import java.util.List;

public class GrupaConverter {

    public static GrupaResponse toGrupaResponse(Grupa grupa) {
        List<PredmetResponse> predmetResponses = new ArrayList<>();
        if (grupa.getPredmeti() != null) {
            grupa.getPredmeti().forEach(predmet ->
                    predmetResponses.add(PredmetConverter.toPredmetResponse(predmet))
            );
        }

        StudijskiProgramResponse spResponse = null;
        if (grupa.getStudijskiProgram() != null) {
            spResponse = new StudijskiProgramResponse(
                    grupa.getStudijskiProgram().getOznaka(),
                    grupa.getStudijskiProgram().getNaziv(),
                    grupa.getStudijskiProgram().getGodinaAkreditacije(),
                    grupa.getStudijskiProgram().getZvanje(),
                    grupa.getStudijskiProgram().getTrajanjeGodina(),
                    grupa.getStudijskiProgram().getTrajanjeSemestara(),
                    grupa.getStudijskiProgram().getVrstaStudija(),
                    grupa.getStudijskiProgram().getUkupnoEspb()
            );
        }

        return new GrupaResponse(grupa.getId(), spResponse, predmetResponses);
    }

    public static List<GrupaResponse> toGrupaResponseList(List<Grupa> grupe) {
        List<GrupaResponse> responseList = new ArrayList<>();
        grupe.forEach(grupa -> responseList.add(toGrupaResponse(grupa)));
        return responseList;
    }
}
