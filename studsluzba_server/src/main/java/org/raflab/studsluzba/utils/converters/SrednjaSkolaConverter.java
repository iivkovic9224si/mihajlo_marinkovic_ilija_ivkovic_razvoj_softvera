package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.SrednjaSkolaRequest;
import org.raflab.studsluzba.controllers.response.SrednjaSkolaResponse;
import org.raflab.studsluzba.model.entity.SrednjaSkola;

import java.util.ArrayList;
import java.util.List;

public class SrednjaSkolaConverter {
    public static SrednjaSkola toSrednjaSkola(SrednjaSkolaRequest request) {
        SrednjaSkola srednjaSkola = new SrednjaSkola();
        srednjaSkola.setNaziv(request.getNaziv());
        srednjaSkola.setVrstaSrednjeSkole(request.getVrstaSrednjeSkole());
        return srednjaSkola;
    }

    public static SrednjaSkolaResponse toSrednjaSkolaResponse(SrednjaSkola srednjaSkola) {
        return new SrednjaSkolaResponse(
                srednjaSkola.getNaziv(),
                srednjaSkola.getVrstaSrednjeSkole()
        );
    }

    public static List<SrednjaSkolaResponse> toSrednjaSkolaResponseList(Iterable<SrednjaSkola> skole) {
        List<SrednjaSkolaResponse> responseList = new ArrayList<>();
        skole.forEach(s -> responseList.add(toSrednjaSkolaResponse(s)));
        return responseList;
    }
}
