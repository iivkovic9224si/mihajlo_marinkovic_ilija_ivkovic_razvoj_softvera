package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.PolozenPredmetAwayRequest;
import org.raflab.studsluzba.controllers.request.PolozenPredmetHereRequest;
import org.raflab.studsluzba.controllers.response.PolozenPredmetResponse;
import org.raflab.studsluzba.model.entity.PolozenPredmet;
import org.raflab.studsluzba.model.entity.VisokoSkolskaUstanova;

public class PolozenPredmetConverter {

    public static PolozenPredmetResponse toResponse(PolozenPredmet entity) {
        if (entity == null) return null;

        return new PolozenPredmetResponse(
                StudentIndexAktivnostConverter.toStudentIndeksResponseWithoutPodaci(entity.getStudentIndeks()),
                PredmetConverter.toPredmetResponse(entity.getPredmet()),
                entity.getIzlazakIspita() != null ? IzlazakNaIspitConverter.toResponse(entity.getIzlazakIspita()) : null,
                entity.getPriznatoOdInstitucije() != null ? VisokoSkolskaUstanovaConverter.toVisokoSkolskaUstanovaResponse(entity.getPriznatoOdInstitucije()) : null,
                entity.isPriznato(),
                entity.getDatum(),
                entity.getNapomena(),
                entity.getBrojPoena(),
                entity.getOcena()
        );
    }

    public static PolozenPredmet fromAwayRequest(PolozenPredmetAwayRequest request, VisokoSkolskaUstanova ustanova) {
        PolozenPredmet pp = new PolozenPredmet();
        pp.setPriznato(true);
        pp.setPriznatoOdInstitucije(ustanova);
        pp.setDatum(request.getDatum());
        pp.setBrojPoena(request.getBrojPoena());
        pp.setNapomena(request.getNapomena());
        pp.setOcena(calculateOcena(request.getBrojPoena()));
        return pp;
    }

    public static PolozenPredmet fromHereRequest(PolozenPredmetHereRequest request) {
        PolozenPredmet pp = new PolozenPredmet();
        pp.setPriznato(false);
        pp.setDatum(null); // može se uzeti iz izlazakIspita
        pp.setNapomena(request.getNapomena());
        return pp;
    }

    public static Integer calculateOcena(double poeni) {
        if (poeni < 51) return 5;
        else if (poeni <= 60) return 6;
        else if (poeni <= 70) return 7;
        else if (poeni <= 80) return 8;
        else if (poeni <= 90) return 9;
        else return 10;
    }
}
