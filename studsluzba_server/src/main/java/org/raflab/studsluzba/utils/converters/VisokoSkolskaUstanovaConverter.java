package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.model.entity.VisokoSkolskaUstanova;
import request.VisokoSkolskaUstanovaRequest;
import response.VisokoSkolskaUstanovaResponse;

import java.util.ArrayList;
import java.util.List;

public class VisokoSkolskaUstanovaConverter {

    public static VisokoSkolskaUstanova toVisokoSkolskaUstanova(VisokoSkolskaUstanovaRequest request) {
        VisokoSkolskaUstanova ustanova = new VisokoSkolskaUstanova();
        ustanova.setSifra(request.getSifra());
        ustanova.setNaziv(request.getNaziv());
        ustanova.setVrsta(request.getVrsta());
        return ustanova;
    }

    public static VisokoSkolskaUstanovaResponse toVisokoSkolskaUstanovaResponse(VisokoSkolskaUstanova ustanova) {
        return new VisokoSkolskaUstanovaResponse(
                ustanova.getSifra(),
                ustanova.getNaziv(),
                ustanova.getVrsta()
        );
    }

    public static List<VisokoSkolskaUstanovaResponse> toVisokoSkolskaUstanovaResponseList(List<VisokoSkolskaUstanova> lista) {
        List<VisokoSkolskaUstanovaResponse> responseList = new ArrayList<>();
        for (VisokoSkolskaUstanova u : lista) {
            responseList.add(toVisokoSkolskaUstanovaResponse(u));
        }
        return responseList;
    }
}
