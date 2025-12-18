package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.model.entity.DrziPredmet;
import org.raflab.studsluzba.model.entity.Ispit;
import org.raflab.studsluzba.model.entity.IspitniRok;
import request.IspitRequest;
import response.DrziPredmetResponse;
import response.IspitResponse;
import response.IspitniRokResponse;
import response.RezultatiIspitaResponse;

public class IspitConverter {
    public static RezultatiIspitaResponse toRezultatiIspitaResponse(Object[] row){
        return new RezultatiIspitaResponse(
                (String) row[0],  // ime
                (String) row[1],  // prezime
                (String) row[2],  // studProgramOznaka
                (Integer) row[3], // godina
                (Integer) row[4], // broj
                (Double) row[5]   // ukupnoPoena
        );
    }
    public static Ispit toIspit(IspitRequest request, DrziPredmet drziPredmet, IspitniRok ispitniRok) {
        Ispit ispit = new Ispit();
        if (request.getId() != null) {
            ispit.setId(request.getId());
        }
        ispit.setDatumIspita(request.getDatumIspita());
        ispit.setVremePocetkaIspita(request.getVremePocetkaIspita());
        ispit.setZakljucen(request.isZakljucen());
        ispit.setDrziPredmet(drziPredmet);
        ispit.setIspitniRok(ispitniRok);
        return ispit;
    }

    public static IspitResponse toIspitResponse(Ispit ispit) {
        IspitResponse response = new IspitResponse();
        response.setId(ispit.getId());
        response.setDatumIspita(ispit.getDatumIspita());
        response.setVremePocetkaIspita(ispit.getVremePocetkaIspita());
        response.setZakljucen(ispit.isZakljucen());
        if (ispit.getDrziPredmet() != null) {
            response.setDrziPredmetResponse(
                    new DrziPredmetResponse(ispit.getDrziPredmet().getSkolskaGodina().getGodina(),
                            ispit.getDrziPredmet().getNastavnik().getEmail(),
                            ispit.getDrziPredmet().getPredmet().getSifra())

            );
        }
        if (ispit.getIspitniRok() != null) {
            response.setIspitniRokResponse(
                    new IspitniRokResponse( ispit.getIspitniRok().getPocetak(),
                            ispit.getIspitniRok().getKraj(),
                            ispit.getIspitniRok().getSkolskaGodina().getGodina()));
        }
        return response;
    }

}
