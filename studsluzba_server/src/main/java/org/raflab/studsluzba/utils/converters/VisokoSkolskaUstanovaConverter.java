package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.VisokoSkolskaUstanovaRequest;
import org.raflab.studsluzba.controllers.response.VisokoSkolskaUstanovaResponse;
import org.raflab.studsluzba.model.entity.VisokoSkolskaUstanova;

import java.util.ArrayList;
import java.util.List;

public class VisokoSkolskaUstanovaConverter {
    public static VisokoSkolskaUstanova toVisokoSkolskaUstanova(VisokoSkolskaUstanovaRequest visokoSkolskaUstanovaRequest) {
        VisokoSkolskaUstanova visokoSkolskaUstanova=new VisokoSkolskaUstanova();
        visokoSkolskaUstanova.setNaziv(visokoSkolskaUstanovaRequest.getNaziv());
        visokoSkolskaUstanova.setVrsta(visokoSkolskaUstanovaRequest.getVrsta());

        return visokoSkolskaUstanova;
    }

    public static VisokoSkolskaUstanovaResponse toVisokoSkolskaUstanovaResponse(VisokoSkolskaUstanova visokoSkolskaUstanova) {
        return new VisokoSkolskaUstanovaResponse(visokoSkolskaUstanova.getNaziv(), visokoSkolskaUstanova.getVrsta().name());
    }

    public static List<VisokoSkolskaUstanovaResponse> toVisokoSkolskaUstanovaResponseList(List<VisokoSkolskaUstanova> vsuList) {
        List<VisokoSkolskaUstanovaResponse> responseList = new ArrayList<>();
        for (VisokoSkolskaUstanova vsu : vsuList) {
            responseList.add(toVisokoSkolskaUstanovaResponse(vsu));
        }
        return responseList;
    }
}
