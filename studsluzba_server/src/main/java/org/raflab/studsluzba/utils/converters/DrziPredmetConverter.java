package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.model.entity.DrziPredmet;
import org.raflab.studsluzba.model.entity.Nastavnik;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import request.DrziPredmetRequest;
import response.DrziPredmetResponse;

import java.util.ArrayList;
import java.util.List;

public class DrziPredmetConverter {
    public static DrziPredmet toDrziPredmet(DrziPredmetRequest request,
                                            Nastavnik nastavnik,
                                            Predmet predmet,
                                            SkolskaGodina skolskaGodina) {
        DrziPredmet dp = new DrziPredmet();
        dp.setNastavnik(nastavnik);
        dp.setPredmet(predmet);
        dp.setSkolskaGodina(skolskaGodina);
        return dp;
    }

    public static DrziPredmetResponse toDrziPredmetResponse(DrziPredmet dp) {
        return new DrziPredmetResponse(
                dp.getSkolskaGodina() != null ? dp.getSkolskaGodina().getGodina() : null,
                dp.getNastavnik() != null ? dp.getNastavnik().getEmail() : null,
                dp.getPredmet() != null ? dp.getPredmet().getSifra() : null

        );
    }



    public static List<DrziPredmetResponse> toDrziPredmetResponseList(Iterable<DrziPredmet> dpList) {
        List<DrziPredmetResponse> responses = new ArrayList<>();
        dpList.forEach(dp -> responses.add(toDrziPredmetResponse(dp)));
        return responses;
    }
}
