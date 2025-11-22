package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;

import java.util.ArrayList;
import java.util.List;

public class PredmetConverter {
    public static Predmet toPredmet(PredmetRequest request, StudijskiProgram studProgram) {
        Predmet predmet = new Predmet();
        predmet.setSifra(request.getSifra());
        predmet.setNaziv(request.getNaziv());
        predmet.setOpis(request.getOpis());
        predmet.setEspb(request.getEspb());
        predmet.setObavezan(request.isObavezan());
        // Postavi postojeći entitet StudijskiProgram
        predmet.setStudProgram(studProgram);
        return predmet;
    }


    public static PredmetResponse toPredmetResponse(Predmet predmet) {
        return new PredmetResponse(
                predmet.getSifra(),
                predmet.getNaziv(),
                predmet.getOpis(),
                predmet.getEspb(),
                predmet.getStudProgram().getOznaka(), // SAMO oznaka kao String
                predmet.isObavezan()
        );
    }



    public static List<PredmetResponse> toPredmetResponseList(List<Predmet> predmetList) {
        List<PredmetResponse> responseList = new ArrayList<>();
        for (Predmet predmet : predmetList) {
            responseList.add(toPredmetResponse(predmet));
        }
        return responseList;
    }
}
