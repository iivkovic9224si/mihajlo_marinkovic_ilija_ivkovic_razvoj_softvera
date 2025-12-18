package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.model.entity.Ispit;
import org.raflab.studsluzba.model.entity.PrijavaIspita;
import org.raflab.studsluzba.model.entity.SlusaPredmet;
import request.PrijavaIspitaRequest;
import response.IspitResponse;
import response.PrijavaIspitaResponse;
import response.SlusaPredmetResponse;

public class PrijavaIspitaConverter {

    // 🔹 Konverzija iz Request-a u entitet
    public static PrijavaIspita toPrijavaIspita(PrijavaIspitaRequest prijavaIspitaRequest, Ispit ispit, SlusaPredmet slusaPredmet) {
        PrijavaIspita prijavaIspita = new PrijavaIspita();
        prijavaIspita.setDatumPrijave(prijavaIspitaRequest.getDatumPrijave());
        prijavaIspita.setIspit(ispit);
        prijavaIspita.setSlusaPredmet(slusaPredmet);
        return prijavaIspita;
    }

    // 🔹 Konverzija iz entiteta u Response DTO
    public static PrijavaIspitaResponse toPrijavaIspitaResponse(PrijavaIspita prijavaIspita) {
        if (prijavaIspita == null) {
            return null;
        }

        IspitResponse ispitResponse = null;
        if (prijavaIspita.getIspit() != null) {
            ispitResponse = IspitConverter.toIspitResponse(prijavaIspita.getIspit());
        }

        SlusaPredmetResponse slusaPredmetResponse = null;
        if (prijavaIspita.getSlusaPredmet() != null) {
            slusaPredmetResponse = SlusaPredmetConverter.toSlusaPredmetResponse(prijavaIspita.getSlusaPredmet());
        }

        return new PrijavaIspitaResponse(
                prijavaIspita.getId(),
                prijavaIspita.getDatumPrijave(),
                ispitResponse,
                slusaPredmetResponse
        );
    }
}
