package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.IspitniRokRequest;
import org.raflab.studsluzba.controllers.response.IspitniRokResponse;
import org.raflab.studsluzba.model.entity.IspitniRok;
import org.raflab.studsluzba.model.entity.SkolskaGodina;

import java.util.ArrayList;
import java.util.List;

public class IspitniRokConverter {

    public static IspitniRok toIspitniRok(IspitniRokRequest request, SkolskaGodina godina) {
        IspitniRok rok = new IspitniRok();
        if(request.getId() != null) {
            rok.setId(request.getId());
        }
        rok.setPocetak(request.getPocetak());
        rok.setKraj(request.getKraj());
        rok.setSkolskaGodina(godina);
        return rok;
    }

    public static IspitniRokResponse toIspitniRokResponse(IspitniRok rok) {
        IspitniRokResponse response = new IspitniRokResponse();
        response.setId(rok.getId());
        response.setPocetak(rok.getPocetak());
        response.setKraj(rok.getKraj());
        if(rok.getSkolskaGodina() != null) {
            response.setGodina(rok.getSkolskaGodina().getGodina());
        }
        return response;
    }

    public static List<IspitniRokResponse> toIspitniRokResponseList(List<IspitniRok> rokovi) {
        List<IspitniRokResponse> list = new ArrayList<>();
        for(IspitniRok rok : rokovi) {
            list.add(toIspitniRokResponse(rok));
        }
        return list;
    }
}
